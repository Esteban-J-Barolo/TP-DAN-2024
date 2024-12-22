package isi.dan.msclientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.msclientes.enums.EstadoObra;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.servicios.ClienteService;
import isi.dan.msclientes.servicios.ObraService;

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
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ObraController.class)
public class ObraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObraService obraService;
    
    @MockBean
    private ClienteService clienteService;

    private Obra obra, obra2, obra3;

    @BeforeEach
    void setUp() {

        obra = new Obra();
        obra.setId(1);
        obra.setCliente(new Cliente());
        obra.setDireccion("Direccion test 352");
        obra.setEsRemodelacion(false);
        obra.setEstado(EstadoObra.PENDIENTE);
        obra.setLat(7265);
        obra.setLng(8246);
        obra.setPresupuesto(BigDecimal.valueOf(2300000));

        obra2 = new Obra();
        obra2.setId(2);
        obra2.setCliente(new Cliente());
        obra2.setDireccion("Direccion test 353");
        obra2.setEstado(EstadoObra.FINALIZADA);

        
        obra3 = new Obra();
        obra3.setId(3);
        obra3.setDireccion("Direccion test 354");
        obra3.setEstado(EstadoObra.HABILITADA);

    }

    @Test
    void testGetAll() throws Exception {

        Mockito.when(obraService.findAll()).thenReturn(Collections.singletonList(obra));

        mockMvc.perform(get("/api/obras"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].direccion").value("Direccion test 352"))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));

    }
    @Test
    void testGetById() throws Exception {
        
        Mockito.when(obraService.findById(1)).thenReturn(Optional.of(obra));

        mockMvc.perform(get("/api/obras/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.direccion").value("Direccion test 352"));
    }

    @Test
    void testGetById_NotFound() throws Exception {

        Mockito.when(obraService.findById(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/obras/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {

        Mockito.when(obraService.save(Mockito.any(Obra.class))).thenReturn(obra);

        mockMvc.perform(post("/api/obras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testUpdate() throws Exception {

        Mockito.when(obraService.findById(1)).thenReturn(Optional.of(obra));
        Mockito.when(obraService.update(Mockito.any(Obra.class))).thenReturn(obra);

        mockMvc.perform(put("/api/obras/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccion").value("Direccion test 352"));
    }

    @Test
    void testDelete() throws Exception {
        Mockito.when(obraService.findById(1)).thenReturn(Optional.of(obra));
        Mockito.doNothing().when(obraService).deleteById(1);

        mockMvc.perform(delete("/api/obras/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFinish() throws Exception {

        Mockito.when(obraService.findById(2)).thenReturn(Optional.of(obra2));
        Mockito.when(obraService.finishObra(Mockito.any(Obra.class))).thenReturn(obra2);

        mockMvc.perform(put("/api/obras/finalizar/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FINALIZADA"));
    }
    
    @Test
    void testEnable() throws Exception {

        Mockito.when(obraService.findById(3)).thenReturn(Optional.of(obra3));
        Mockito.when(obraService.enableObra(Mockito.any(Obra.class))).thenReturn(obra3);

        mockMvc.perform(put("/api/obras/habilitar/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("HABILITADA"));
    }

    @Test
    void testPending() throws Exception {

        Mockito.when(obraService.findById(1)).thenReturn(Optional.of(obra));
        Mockito.when(obraService.pendingObra(Mockito.any(Obra.class))).thenReturn(obra);

        mockMvc.perform(put("/api/obras/pendiente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(obra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

