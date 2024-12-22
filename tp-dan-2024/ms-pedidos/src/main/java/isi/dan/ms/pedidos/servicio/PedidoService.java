package isi.dan.ms.pedidos.servicio;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import isi.dan.ms.pedidos.conf.RabbitMQConfig;
import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.PedidoUpdateDTO;
import isi.dan.ms.pedidos.dto.StockUpdateDTO;
import isi.dan.ms.pedidos.exception.PedidoNotFoundException;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.HistorialPedido;
import isi.dan.ms.pedidos.modelo.Obra;
import isi.dan.ms.pedidos.modelo.Pedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Configuration
public class PedidoService {

    private RestTemplate restTemplate;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // @Autowired
    public PedidoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Logger log = LoggerFactory.getLogger(PedidoService.class);

    public Pedido savePedido(Pedido pedido) {
        for( DetallePedido dp : pedido.getDetalle()){
            log.info("Enviando {}", dp.getProducto().getId()+";"+dp.getCantidad());
            rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_UPDATE_QUEUE, "{\"idProducto\":"+dp.getProducto().getId()+",\n \"cantidad\":"+dp.getCantidad()+"}");
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido updatePedido(PedidoUpdateDTO pedido) throws PedidoNotFoundException{
        Pedido pedidoBuscado = this.getPedidoById(pedido.getId());
        pedidoBuscado.setObservaciones(pedido.getObservaciones());
        pedidoBuscado.setTotal(pedido.getTotal());
        pedidoBuscado.setEstado(pedido.getEstado());
        return pedidoRepository.save(pedidoBuscado);
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(String id) throws PedidoNotFoundException {
        return pedidoRepository.findById(id).orElseThrow(() -> new PedidoNotFoundException(id));
    }

    public void deletePedido(String id) {
        pedidoRepository.deleteById(id);
    }

    private int saldoDisponibleDeCliente(Integer clienteId, BigDecimal montoTotal) {
        try {

            String url = "http://MS-CLIENTES/api/clientes/" + clienteId + "/verificar-monto?monto="+montoTotal;

            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            
            if (response.getBody()){
                System.out.println("El monto del cliente alcanza.");
                return 1;
            }else{
                System.out.println("El monto del cliente no alncanza.");
                return 0;
            }

        } catch (RestClientException e) {
            System.out.println("Error al verificar monto del cliente: " + e);
            return -1;
        }
    }
    private boolean verificarYActualizarStock(List<DetallePedido> detallesPedido){
        try {
            for (DetallePedido detalle : detallesPedido) {
                String url = "http://MS-PRODUCTOS/api/productos/" + detalle.getProducto().getId() + "/verificar-stock";

                // Crear el DTO con los datos del detalle del pedido
                StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
                stockUpdateDTO.setIdProducto(detalle.getProducto().getId());
                stockUpdateDTO.setCantidad(detalle.getCantidad());

                // Realizar una solicitud POST al servicio de productos
                ResponseEntity<Boolean> response = restTemplate.postForEntity(url, stockUpdateDTO, Boolean.class);

                // Verificar la respuesta
                if (response.getStatusCode() != HttpStatus.OK || !Boolean.TRUE.equals(response.getBody())) {
                    return false; // Si algún producto no tiene stock suficiente, devolvemos false
                }
            }
            return true; // Todos los productos tienen stock suficiente
        } catch (RestClientException e) {
            throw new RuntimeException("Error al verificar o actualizar el stock", e);
        }

    }
    public Pedido createPedido(Cliente cliente, Obra obra, List<DetallePedido> detallesPedido, String observacion){
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(sequenceGenerator.generateSequence("secPedidos"));
        pedido.setCliente(cliente);
        pedido.setObra(obra);
        pedido.setObservaciones(observacion);
        pedido.setDetalle(detallesPedido);
        pedido.setUsuario(cliente.getNombre());
        
        // monto total
        detallesPedido.forEach(detalle -> {
            BigDecimal precioBase = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            
            BigDecimal descuentoMonto = precioBase
                .multiply(detalle.getDescuento().divide(BigDecimal.valueOf(100)));
            
            BigDecimal precioFinal = precioBase.subtract(descuentoMonto);
            
            detalle.setPrecioFinal(precioFinal);
        });
        BigDecimal montoTotal = detallesPedido.stream()
                                                .map(DetallePedido::getPrecioFinal)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setTotal(montoTotal);

        // ----------------------------------------------------------
        // buscar todos los pedidos del cliente
        // quedarse solo con los que tengan estado aceptado o en preparación
        List<Pedido> pedidosCliente = pedidoRepository.findByClienteId(cliente.getId());
        BigDecimal montoTotalPedidos = pedidosCliente.stream()
                                                        .filter(ped -> ped.getEstado() == EstadoPedido.ACEPTADO || 
                                                                    ped.getEstado() == EstadoPedido.EN_PREPARACION)
                                                        .map(Pedido::getTotal)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        montoTotal = montoTotal.add(montoTotalPedidos);

        int montoSuficiente = saldoDisponibleDeCliente(cliente.getId(), montoTotal);

        if(montoSuficiente == 1){
            pedido.setEstado(EstadoPedido.ACEPTADO);
            pedido.agregarEstado(new HistorialPedido(Instant.now(), EstadoPedido.ACEPTADO));

            // verificar y actualizar stock
            boolean stockActualizado = verificarYActualizarStock(detallesPedido);

            if (stockActualizado) {
                pedido.setEstado(EstadoPedido.EN_PREPARACION);
                pedido.agregarEstado(new HistorialPedido(Instant.now(), EstadoPedido.EN_PREPARACION));

                // enviar los mensajes para actualizar el stock y guardar el pedido
                this.savePedido(pedido);
            }else{
                pedidoRepository.save(pedido);
            }

        }else{
            if( montoSuficiente == 0 ){
                pedido.setEstado(EstadoPedido.RECHAZADO);
                pedido.agregarEstado(new HistorialPedido(Instant.now(), EstadoPedido.RECHAZADO));
    
                pedidoRepository.save(pedido);
            }else{
                pedido.setEstado(EstadoPedido.RECIBIDO);
                pedido.agregarEstado(new HistorialPedido(Instant.now(), EstadoPedido.RECIBIDO));
    
                pedidoRepository.save(pedido);
            }
        }
        
        return pedido;

    }

    public boolean cancelarPedido(Pedido pedido){
        if(pedido.getEstado() == EstadoPedido.EN_PREPARACION){
            // devolver el stock de los productos
            for( DetallePedido dp : pedido.getDetalle()){
                log.info("Devolviendo {}", dp.getProducto().getId()+";"+dp.getCantidad());
                rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_UPDATE_QUEUE, dp.getProducto().getId()+";"+(dp.getCantidad()*-1));
            }
        }
        pedido.setEstado(EstadoPedido.CANCELADO);
        pedido.agregarEstado(new HistorialPedido(Instant.now(), EstadoPedido.CANCELADO));
        pedidoRepository.save(pedido);
        return true;
    }

    public List<Pedido> getPedidosDeCliente(Integer id){
        return pedidoRepository.findByClienteId(id);
    }

    public Page<Pedido> buscarPedidos(Integer numero, Integer clienteId, EstadoPedido estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pedidoRepository.buscarPorCriterios(numero, clienteId, estado, pageable);
    } 
}
