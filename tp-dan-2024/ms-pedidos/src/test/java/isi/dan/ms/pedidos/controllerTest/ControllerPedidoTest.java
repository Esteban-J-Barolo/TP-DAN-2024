package isi.dan.ms.pedidos.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import isi.dan.ms.pedidos.controller.PedidoController;
import isi.dan.ms.pedidos.dto.PedidoRequest;
import isi.dan.ms.pedidos.dto.PedidoUpdateDTO;
import isi.dan.ms.pedidos.exception.PedidoNotFoundException;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Obra;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.modelo.Producto;
import isi.dan.ms.pedidos.servicio.PedidoService;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@ExtendWith(MockitoExtension.class)
public class ControllerPedidoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    private Pedido pedido1, pedido2;
    private PedidoRequest pedidoRequest;

    
    @BeforeEach
    void setUp(){
        // Configuramos MockMvc para realizar solicitudes HTTP simuladas.
        pedidoRequest = new PedidoRequest();
        pedidoRequest.setCliente(new Cliente());
        pedidoRequest.setObra(new Obra());
        pedidoRequest.setDetallesPedido(new ArrayList<>());
        pedidoRequest.setObservacion("Test observacion");

        pedido1 = new Pedido();
        pedido1.setId("1");
        pedido1.setNumeroPedido(123);
        pedido1.setCliente(new Cliente());
        pedido1.setObra(new Obra());
        pedido1.setTotal(BigDecimal.valueOf(1000));
        pedido1.setEstado(EstadoPedido.ENTREGADO);
        
        pedido2 = new Pedido();
        pedido2.setId("2");
        pedido2.setNumeroPedido(456);
        pedido2.setCliente(new Cliente());
        pedido2.setObra(new Obra());
        pedido2.setTotal(BigDecimal.valueOf(2000));
        pedido2.setEstado(EstadoPedido.ENTREGADO);
    }

    @Test
    void testCreatePedido() throws Exception {  

        // Simulamos que el servicio devuelve el pedido cuando se llama a createPedido
        Mockito.when(pedidoService.createPedido(
            pedidoRequest.getCliente(), 
            pedidoRequest.getObra(), 
            pedidoRequest.getDetallesPedido(), 
            pedidoRequest.getObservacion()))
            .thenReturn(pedido1);

        // Realizamos la solicitud POST a /pedidos
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(pedidoRequest)))  // Convierte el objeto a JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.numeroPedido").value(123))
                .andExpect(jsonPath("$.total").value(1000));
    }

    @Test
    void testGetAllPedidos() throws Exception {

        List<Pedido> pedidos = Collections.singletonList(pedido1);
        
        Mockito.when(pedidoService.getAllPedidos()).thenReturn(pedidos);

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void testGetPedidoById() throws Exception {
        String pedidoId = "1";

        Mockito.when(pedidoService.getPedidoById(pedidoId)).thenReturn(pedido1);

        mockMvc.perform(get("/api/pedidos/{id}", pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoId)) 
                .andExpect(jsonPath("$.numeroPedido").value(123))
                .andExpect(jsonPath("$.total").value(1000));
    }

    @Test
    void testGetPedidoById_NotFound() throws Exception {
        String pedidoId = "999";

        Mockito.when(pedidoService.getPedidoById(pedidoId)).thenReturn(null);

        mockMvc.perform(get("/api/pedidos/{id}", pedidoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePedido() throws Exception {
        String pedidoId = "1";

        Mockito.doNothing().when(pedidoService).deletePedido(pedidoId);

        mockMvc.perform(delete("/api/pedidos/{id}", pedidoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdatePedido() throws Exception {
        
        PedidoUpdateDTO pedidoUpdateDTO = new PedidoUpdateDTO();
        pedidoUpdateDTO.setId("1");
        pedidoUpdateDTO.setEstado(EstadoPedido.ENTREGADO);
        pedidoUpdateDTO.setObservaciones("Actualización de observación");
        pedidoUpdateDTO.setTotal(new BigDecimal(234));

        Pedido pedidoUpdated = new Pedido();
        pedidoUpdated.setId("1");
        pedidoUpdated.setEstado(EstadoPedido.ENTREGADO);
        pedidoUpdated.setObservaciones("Actualización de observación");
        pedidoUpdated.setTotal(new BigDecimal(234));

        Mockito.when(pedidoService.updatePedido(Mockito.any(PedidoUpdateDTO.class)))
            .thenReturn(pedidoUpdated);

        mockMvc.perform(put("/api/pedidos/actualizar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(pedidoUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.estado").value("ENTREGADO"))
                .andExpect(jsonPath("$.observaciones").value("Actualización de observación"))
                .andExpect(jsonPath("$.total").value(new BigDecimal(234)));
    }

    @Test
    void testUpdatePedido_NotFound() throws Exception {
        
        PedidoUpdateDTO pedidoUpdateDTO = new PedidoUpdateDTO();
        pedidoUpdateDTO.setId("999");
        pedidoUpdateDTO.setEstado(EstadoPedido.ENTREGADO);
        pedidoUpdateDTO.setObservaciones("Actualización de observación");
        pedidoUpdateDTO.setTotal(new BigDecimal(234));

        Mockito.when(pedidoService.updatePedido(pedidoUpdateDTO))
            .thenThrow(new PedidoNotFoundException(pedidoUpdateDTO.getId()));

        mockMvc.perform(put("/api/pedidos/actualizar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(pedidoUpdateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido 999 no encontrado"));
    }

    @Test
    void testGetEstadoById_Success() throws Exception {

        Mockito.when(pedidoService.getPedidoById("1")).thenReturn(pedido1);

        mockMvc.perform(get("/api/pedidos/estado/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("ENTREGADO"));
    }

    @Test
    void testGetEstadoById_NotFound() throws Exception {
        
        Mockito.when(pedidoService.getPedidoById("999")).thenThrow(new PedidoNotFoundException("999"));

        mockMvc.perform(get("/api/pedidos/estado/{id}", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido 999 no encontrado"));
    }

    @Test
    void testGetPedidosDeClienteById() throws Exception {

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        Mockito.when(pedidoService.getPedidosDeCliente(Mockito.anyInt())).thenReturn(pedidos);

        mockMvc.perform(get("/api/pedidos/cliente/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].numeroPedido").value(123))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].numeroPedido").value(456));
    }

    @Test
    void testPutCencelarPedido() throws Exception {

        Mockito.when(pedidoService.getPedidoById("1")).thenReturn(pedido1);
        
        Mockito.when(pedidoService.cancelarPedido(pedido1)).thenReturn(true);

        mockMvc.perform(put("/api/pedidos/cancelar/{id}", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPutCencelarPedido_NotFound() throws Exception {
        
        Mockito.when(pedidoService.getPedidoById("999")).thenThrow(new PedidoNotFoundException("999"));

        mockMvc.perform(put("/api/pedidos/cancelar/{id}", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido 999 no encontrado"));
    }

    @Test
    void testBuscarPedidosPorCriterios() throws Exception {

        List<Pedido> pedidosList = Arrays.asList(pedido1, pedido2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pedido> pagePedidos = new PageImpl<>(pedidosList, pageable, pedidosList.size());

        Mockito.when(pedidoService.buscarPedidos(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(pagePedidos);

        mockMvc.perform(get("/api/pedidos/buscar")
                        .param("estado", "ENTREGADO")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].numeroPedido").value(123))
                .andExpect(jsonPath("$.content[1].numeroPedido").value(456));
    }

    @Test
    void testGetEstadosPedido() throws Exception {
        
        mockMvc.perform(get("/api/pedidos/estados-pedido"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.length()").value(EstadoPedido.values().length))
                .andExpect(jsonPath("$[0]").value(EstadoPedido.RECHAZADO.toString()))
                .andExpect(jsonPath("$[1]").value(EstadoPedido.ACEPTADO.toString()));
    }
    
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
