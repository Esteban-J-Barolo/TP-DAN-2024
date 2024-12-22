package isi.dan.ms.pedidos.data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoObra;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.HistorialPedido;
import isi.dan.ms.pedidos.modelo.Obra;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.modelo.Producto;

@Component
public class PedidoDataLoader implements CommandLineRunner {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Limpiar datos existentes
        pedidoRepository.deleteAll();

        List<Pedido> pedidosIniciales = new ArrayList<Pedido>();

        IntStream.range(1, 11).forEach(i -> {

            Cliente cliente = new Cliente();
            cliente.setId(i);
            cliente.setNombre("Cliente " + i);
            cliente.setCorreoElectronico("cliente" + i + "@example.com");
            cliente.setCuit("20-" + (10000000 + i) + "-9");
            cliente.setMaximoDescubierto(BigDecimal.valueOf(10000 + i * 1000));

            Obra obra = new Obra();
            obra.setId(i);
            obra.setDireccion("Dirección " + i + " del cliente " + i);
            obra.setEsRemodelacion(i % 2 == 0);
            obra.setLat(-34.6f + i * 0.01f);
            obra.setLng(-58.4f + i * 0.01f);
            obra.setPresupuesto(BigDecimal.valueOf(5000 + i * 1000));
            obra.setEstado(EstadoObra.HABILITADA);
            obra.setCliente(cliente);

            // Crear y guardar obras para cada cliente
            List<Producto> productosSeleccionados = new ArrayList<Producto>();
            IntStream.range(1, 4).forEach(j -> {
                Producto producto = new Producto();
                producto.setId((long)i+(long)j);
                producto.setNombre("Producto " + j);
                producto.setPrecio(BigDecimal.valueOf(50 + j * 100));
                producto.setDescripcion("Descripción " + j);
                productosSeleccionados.add(producto);
            });

            pedidosIniciales.add(crearPedidoEjemplo(cliente, obra, productosSeleccionados, "Observación "+i, i, "Usuario "+i));
        });

        pedidoRepository.saveAll(pedidosIniciales);
        System.out.println("Pedidos de ejemplo cargados: " + pedidosIniciales.size());
    }

    private Pedido crearPedidoEjemplo(Cliente cliente, Obra obra, List<Producto> productosSeleccionados, String observacion, Integer numeroPedido, String usuario) {
        
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setObra(obra);
        pedido.setObservaciones(observacion);
        pedido.setEstado(EstadoPedido.RECIBIDO);
        pedido.setNumeroPedido(numeroPedido);
        pedido.setUsuario(usuario);
        
        // Crear detalles de pedido
        List<DetallePedido> detalles = productosSeleccionados.stream()
            .map(producto -> {
                DetallePedido detalle = new DetallePedido();
                detalle.setProducto(producto);
                detalle.setCantidad(new Random().nextInt(5) + 1); // Cantidad aleatoria entre 1 y 5
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setDescuento(BigDecimal.valueOf(new Random().nextInt(10))); // Descuento aleatorio hasta 10%
                
                // Calcular precio final
                BigDecimal precioBase = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
                BigDecimal descuentoMonto = precioBase
                    .multiply(detalle.getDescuento().divide(BigDecimal.valueOf(100)));
                detalle.setPrecioFinal(precioBase.subtract(descuentoMonto));
                
                return detalle;
            })
            .collect(Collectors.toList());
        
        pedido.setDetalle(detalles);

        // Calcular total
        BigDecimal total = detalles.stream()
            .map(DetallePedido::getPrecioFinal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setTotal(total);

        // Agregar historial de estado
        List<HistorialPedido> historial = new ArrayList<>();
        historial.add(new HistorialPedido(Instant.now(), EstadoPedido.RECIBIDO));
        pedido.setEstados(historial);

        return pedido;
    }
}
