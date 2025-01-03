package isi.dan.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.servicios.ClienteService;
import isi.dan.msclientes.servicios.ObraService;
import isi.dan.msclientes.servicios.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.springframework.test.context.ActiveProfiles;

// @ActiveProfiles("db")
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;
    
    @MockBean
    private ObraService obraService;
    
    @MockBean
    private UsuarioService usuarioService;

    private Cliente cliente, cliente2;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Test Cliente");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("23413598249");
        cliente.setMaximoDescubierto(BigDecimal.valueOf(200000));
        cliente.setMaximoObrasActivas(10);
        
        cliente2 = new Cliente();
        cliente2.setId(2);
        cliente2.setNombre("Test Cliente 2");
        cliente2.setCorreoElectronico("test2@cliente.com");
        cliente2.setCuit("23413598248");
        cliente2.setMaximoDescubierto(BigDecimal.valueOf(30000));
        cliente2.setMaximoObrasActivas(1);
    }

    @Test
    void testGetAll() throws Exception {
        
        List<Cliente> clientes = Collections.singletonList(cliente);
        
        Mockito.when(clienteService.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Test Cliente"))
                .andExpect(jsonPath("$[0].correoElectronico").value("test@cliente.com"));
    }

    @Test
    void testGetById() throws Exception {
        
        Mockito.when(clienteService.findById(1)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Test Cliente"))
                .andExpect(jsonPath("$.cuit").value("23413598249"));
    }

    @Test
    void testGetById_NotFound() throws Exception {

        Mockito.when(clienteService.findById(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        
        Mockito.when(clienteService.save(Mockito.any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Cliente"))
                .andExpect(jsonPath("$.maximoDescubierto").value(BigDecimal.valueOf(200000)));
    }

    // @Test
    // void testCreate_NotFound() throws Exception {
        
    //     Mockito.when(clienteService.save(Mockito.any(Cliente.class))).thenReturn(cliente2);

    //     mockMvc.perform(post("/api/clientes")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(asJsonString(cliente2)))
    //             .andExpect(status().isInternalServerError());
    // }

    @Test
    void testUpdate() throws Exception {

        Mockito.when(clienteService.findById(1)).thenReturn(Optional.of(cliente));
        Mockito.when(clienteService.update(Mockito.any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test Cliente"));
    }

    @Test
    void testDelete() throws Exception {

        Mockito.when(clienteService.findById(1)).thenReturn(Optional.of(cliente));
        Mockito.doNothing().when(clienteService).deleteById(1);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

