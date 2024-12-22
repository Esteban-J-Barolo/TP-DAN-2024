package isi.dan.msclientes.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.model.Usuario;
import isi.dan.msclientes.servicios.ClienteService;
import isi.dan.msclientes.servicios.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ClienteService clienteService;

    private Usuario usuario, usuario2;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Test usuario nombre");
        usuario.setApellido("Test usuario apeliido");
        usuario.setCorreoElectronico("test@usuario.com");
        usuario.setDni("41359824");
        
        usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNombre("Test usuario nombre 2");
        usuario2.setApellido("Test usuario apeliido 2");
        usuario2.setCorreoElectronico("test2@usuario.com");
        usuario2.setDni("4135982412");
    }

    @Test
    void testGetAll() throws Exception {
        
        Mockito.when(usuarioService.findAll()).thenReturn(Collections.singletonList(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Test usuario nombre"))
                .andExpect(jsonPath("$[0].correoElectronico").value("test@usuario.com"));
    }

    @Test
    void testGetById() throws Exception {
        
        Mockito.when(usuarioService.findById(1)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Test usuario nombre"))
                .andExpect(jsonPath("$.dni").value("41359824"));
    }

    @Test
    void testGetById_NotFound() throws Exception {

        Mockito.when(usuarioService.findById(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        
        Mockito.when(usuarioService.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test usuario nombre"));
    }

    @Test
    void testCreate_NotFound() throws Exception {
        
        Mockito.when(usuarioService.save(Mockito.any(Usuario.class))).thenReturn(usuario2);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usuario2)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdate() throws Exception {

        Mockito.when(usuarioService.findById(1)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioService.update(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Test usuario nombre"));
    }

    @Test
    void testDelete() throws Exception {

        Mockito.when(usuarioService.findById(1)).thenReturn(Optional.of(usuario));
        Mockito.doNothing().when(usuarioService).deleteById(1);

        mockMvc.perform(delete("/api/usuarios/1"))
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

