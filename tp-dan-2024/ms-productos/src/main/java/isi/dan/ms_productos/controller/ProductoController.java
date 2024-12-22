package isi.dan.ms_productos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.client.RestTemplate;


import isi.dan.ms_productos.aop.LogExecutionTime;
import isi.dan.ms_productos.dto.Provision;
import isi.dan.ms_productos.dto.StockUpdateDTO;
import isi.dan.ms_productos.exception.ProductoNotFoundException;
import isi.dan.ms_productos.modelo.Categoria;
import isi.dan.ms_productos.modelo.Producto;
import isi.dan.ms_productos.servicio.EchoClientFeign;
import isi.dan.ms_productos.servicio.ProductoService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    EchoClientFeign echoSvc;


    @PostMapping
    @LogExecutionTime
    public ResponseEntity<Producto> createProducto(@RequestBody @Validated Producto producto) {
        Producto savedProducto = productoService.saveProducto(producto);
        return ResponseEntity.ok(savedProducto);
    }

    // @GetMapping("/test")
    // @LogExecutionTime
    // public String getEcho() {
    //     String resultado = echoSvc.echo();
    //     log.info("Log en test 1!!!! {}",resultado);
    //     return resultado;
    // }

    // @GetMapping("/test2")
    // @LogExecutionTime
    // public String getEcho2() {
    //     RestTemplate restTemplate = new RestTemplate();
    //     String gatewayURL = "http://ms-gateway-svc:8080";
    //     String resultado = restTemplate.getForObject(gatewayURL+"/clientes/api/clientes/echo", String.class);
    //     log.info("Log en test 2 {}",resultado);
    //     return resultado;
    // }

    @GetMapping
    @LogExecutionTime
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @GetMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) throws ProductoNotFoundException {
        return  ResponseEntity.ok(productoService.getProductoById(id));
    }

    @DeleteMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizar")
    @LogExecutionTime
    public ResponseEntity<Producto> update(@RequestBody Producto producto) throws ProductoNotFoundException {
        productoService.getProductoById(producto.getId());
        return ResponseEntity.ok(productoService.update(producto));
    }

    @PutMapping("/provision/{id}")
    @LogExecutionTime
    public ResponseEntity<Producto> provision(@PathVariable final Long id, @RequestBody Provision provision) throws ProductoNotFoundException {
        Producto producto = productoService.getProductoById(id);
        producto.actualizar(provision);
        return ResponseEntity.ok(productoService.update(producto));
    }

    @PutMapping("/promocion/{id}")
    @LogExecutionTime
    public ResponseEntity<Producto> promocion(@PathVariable final Long id, @RequestBody BigDecimal descuento) throws ProductoNotFoundException {
        Producto producto = productoService.getProductoById(id);
        producto.setDescuento(descuento);
        return ResponseEntity.ok(productoService.update(producto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Producto>> buscarProductoPorParametros(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) Integer stockMin,
            @RequestParam(required = false) Integer stockMax, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
                try {
                    Page<Producto> productos = productoService.buscarProductos(
                        nombre, 
                        codigo, 
                        precioMin, 
                        precioMax, 
                        stockMin, 
                        stockMax, 
                        page, 
                        size
                    );
                    return ResponseEntity.ok(productos);
                } catch (Exception e) {
                    log.error("Error al buscar productos: ", e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
    }

    @PostMapping("{id}/verificar-stock")
    public ResponseEntity<Boolean> verificarYActualizarStock(@PathVariable Long id, @RequestBody StockUpdateDTO stockUpdate) throws ProductoNotFoundException {
        
        Producto producto = productoService.getProductoById(id);

        if (producto.getStockActual() >= stockUpdate.getCantidad()) {
            // Actualizar el stock
            // producto.setStockActual(producto.getStockActual() - stockUpdate.getCantidad());
            // productoService.saveProducto(producto);
            return ResponseEntity.ok(true); // Stock suficiente y actualizado
        } else {
            return ResponseEntity.ok(false); // Stock insuficiente
        }
    }

    @GetMapping("/categorias")
    public ResponseEntity<Categoria[]> getCategoriasProductos(){
        return ResponseEntity.ok(Categoria.values());
    }

}

