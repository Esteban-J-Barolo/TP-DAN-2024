package isi.dan.ms_productos.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.ms_productos.conf.RabbitMQConfig;
import isi.dan.ms_productos.dao.ProductoRepository;
import isi.dan.ms_productos.dto.StockUpdateDTO;
import isi.dan.ms_productos.exception.ProductoNotFoundException;
import isi.dan.ms_productos.modelo.Producto;
// import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

// @Slf4j
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    Logger log = LoggerFactory.getLogger(ProductoService.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper(); // Para deserializar mensajes JSON

    @RabbitListener(queues = RabbitMQConfig.STOCK_UPDATE_QUEUE)
    public void handleStockUpdate(Message msg) {
        // log.info("Recibido {}", msg);
        // buscar el producto
        // actualizar el stock
        // verificar el punto de pedido y generar un pedido
        try {
            log.info("Mensaje recibido: {}", new String(msg.getBody()));

            // 1. Deserializar el mensaje a un objeto
            StockUpdateDTO stockUpdate = objectMapper.readValue(msg.getBody(), StockUpdateDTO.class);

            // 2. Buscar el producto por ID
            Producto producto = productoRepository.findById(stockUpdate.getIdProducto()).orElseThrow(() -> new ProductoNotFoundException(stockUpdate.getIdProducto()));

            // 3. Actualizar el stock
            int stockActualizado = producto.getStockActual() - stockUpdate.getCantidad();
            if(stockActualizado <= 0 ){
                // mensaje de que no hay stock
            }else{
                producto.setStockActual(stockActualizado);
                productoRepository.save(producto);
            }

            log.info("Stock actualizado para el producto {}: nuevo stock = {}", producto.getId(), producto.getStockActual());

            // 4. Verificar el punto de pedido
            if (producto.getStockActual() <= producto.getStockMinimo()) {
                log.info("El stock del producto {} está por debajo del punto de pedido. Generando pedido...", producto.getId());
                // pedidoService.generarPedido(producto);
            }

        } catch (Exception e) {
            log.error("Error procesando mensaje: {}", e.getMessage(), e);
            // Manejo de errores, opcionalmente enviar a una Dead Letter Queue
        }
    }

    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Long id) throws ProductoNotFoundException{
        return productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
    }

    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto update(Producto producto){
        return productoRepository.save(producto);
    }

    // public Producto[] buscarProductosPor(){
    public Page<Producto> buscarProductos(String nombre, String codigo, BigDecimal precioMin, BigDecimal precioMax, Integer stockMin, Integer stockMax, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.buscarPorCriterios(nombre, codigo, precioMin, precioMax, stockMin, stockMax, pageable);
    }
}

