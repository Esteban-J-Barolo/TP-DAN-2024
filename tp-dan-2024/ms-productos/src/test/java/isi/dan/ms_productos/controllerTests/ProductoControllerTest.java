package isi.dan.ms_productos.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import isi.dan.ms_productos.controller.ProductoController;
import isi.dan.ms_productos.dto.Provision;
import isi.dan.ms_productos.exception.ProductoNotFoundException;
import isi.dan.ms_productos.modelo.Categoria;
import isi.dan.ms_productos.modelo.Producto;
import isi.dan.ms_productos.servicio.EchoClientFeign;
import isi.dan.ms_productos.servicio.ProductoService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    // @MockBean
    // private ProductoRepository productoRepository;
    
    // @Import(FeignClientsConfiguration.class)
    @MockBean
    private EchoClientFeign echoSvc;

    private Producto producto, producto2;

    private Provision provision;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Portland");
        producto.setCodigo("0f1");
        producto.setDescripcion("Descripcion prod 1");
        producto.setCategoria(Categoria.CEMENTOS);
        producto.setPrecio(BigDecimal.valueOf(23000.0));
        producto.setDescuento(BigDecimal.valueOf(2399.99));
        producto.setStockActual(54);
        producto.setStockMinimo(20);
        
        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Cal");
        producto2.setCodigo("0f2");
        producto2.setDescripcion("Descripcion prod 2");
        producto2.setCategoria(Categoria.CEMENTOS);
        producto2.setPrecio(BigDecimal.valueOf(33000.0));
        producto2.setDescuento(BigDecimal.valueOf(5599.99));
        producto2.setStockActual(34);
        producto2.setStockMinimo(15);

        objectMapper = new ObjectMapper();
        provision = new Provision();
        provision.setStockRecibido(10);
    }

    // createProducto
    @Test
    void testCreateProducto() throws Exception {
        
        Mockito.when(productoService.saveProducto(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Portland"))
                .andExpect(jsonPath("$.precio").value(BigDecimal.valueOf(23000.0)));
    }

    // getAllProductos
    @Test
    void testGetAllProductos() throws Exception {

        List<Producto> productos = Collections.singletonList(producto);
        
        Mockito.when(productoService.getAllProductos()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Portland"))
                .andExpect(jsonPath("$[0].precio").value(BigDecimal.valueOf(23000.0)));
    }

    // getProductoById
    @Test
    void testGetProductoById() throws Exception {
        
        Mockito.when(productoService.getProductoById(Long.parseLong("1"))).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Portland"))
                .andExpect(jsonPath("$.precio").value(BigDecimal.valueOf(23000.0)));

    }

    @Test
    void testGetProductoById_NotFound() throws Exception {

        Long id = 3L;

        Mockito.when(productoService.getProductoById(id)).thenThrow(new ProductoNotFoundException(id));

        mockMvc.perform(get("/api/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                // Verificar que el mensaje de error es el esperado
                String responseBody = result.getResponse().getContentAsString();
                assertTrue(responseBody.contains("Producto "+id+" no encontrado"));
            });
    }

    // deleteProducto
    @Test
    void testDeleteProducto() throws Exception {

        Long id = 1L;

        Mockito.doNothing().when(productoService).deleteProducto(id);

        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isNoContent());

    }

    // update
    @Test
    void testUpdate() throws Exception {

        Long id = 1L;

        Mockito.when(productoService.getProductoById(id)).thenReturn(producto);
        Mockito.when(productoService.update(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Portland"));
    }

    // provision
    @Test
    void testProvision_Success() throws Exception {

        int stockAnteriorProducto = producto.getStockActual();
        Long id = producto.getId();

        Mockito.when(productoService.getProductoById(id)).thenReturn(producto);
        Mockito.when(productoService.update(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/provision/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(provision)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.stockActual").value(stockAnteriorProducto+provision.getStockRecibido()));
    }

    @Test
    void testProvision_ProductoNotFound() throws Exception {
        
        Mockito.when(productoService.getProductoById(1L))
               .thenThrow(new ProductoNotFoundException(1L));

        mockMvc.perform(put("/api/productos/provision/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(provision)))
                .andExpect(status().isNotFound());
    }

    // promocion
    @Test
    void testPromocion_Success() throws Exception {
        BigDecimal descuento = new BigDecimal("1000");

        Mockito.when(productoService.getProductoById(1L)).thenReturn(producto);
        
        Mockito.when(productoService.update(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/promocion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(descuento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descuento").value(descuento));
    }

    @Test
    void testPromocion_ProductoNotFound() throws Exception {
        BigDecimal descuento = new BigDecimal("1000.00");

        Mockito.when(productoService.getProductoById(1L))
               .thenThrow(new ProductoNotFoundException(1L));

        mockMvc.perform(put("/api/productos/promocion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(descuento)))
                .andExpect(status().isNotFound());
    }

    // buscarClientesPorNombre
    @Test
    void testBuscarProductos_Success() throws Exception {
        
        Page<Producto> paginaProductos = new PageImpl<>(
            List.of(producto2),
            PageRequest.of(0, 10),
            2 // total elementos
        );

        Mockito.when(productoService.buscarProductos(
            eq("Cal"),  // categoría específica
            isNull(),       // nombre null o el valor que esperas
            isNull(),       // otros parámetros que no se usan en este test
            isNull(),
            isNull(),
            isNull(),
            eq(0),         // página
            eq(10)         // tamaño
        )).thenReturn(paginaProductos);

        // Realizar la solicitud GET de búsqueda
        mockMvc.perform(get("/api/productos/buscar")
                .param("nombre", "Cal")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nombre").value("Cal"));
    }

    @Test
    void testBuscarProductos_NoResults() throws Exception {
        // Crear página vacía
        Page<Producto> paginaVacia = new PageImpl<>(
            Collections.emptyList(),
            PageRequest.of(0, 10),
            0
        );

        Mockito.when(productoService.buscarProductos(
            eq("CATEGORIA_INEXISTENTE"),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            eq(0),
            eq(10)
        )).thenReturn(paginaVacia);

        mockMvc.perform(get("/api/productos/buscar")
                .param("nombre", "CATEGORIA_INEXISTENTE")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
