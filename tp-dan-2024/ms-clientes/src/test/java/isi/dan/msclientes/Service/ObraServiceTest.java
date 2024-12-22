package isi.dan.msclientes.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.enums.EstadoObra;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.servicios.ObraService;

@ExtendWith(MockitoExtension.class)
public class ObraServiceTest {

    @Mock
    private ObraRepository obraRepository;

    @InjectMocks
    private ObraService obraService;

    private Obra obra;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Cliente Test");
        cliente.setMaximoObrasActivas(5);

        obra = new Obra();
        obra.setId(1);
        obra.setDireccion("Direccion Test");
        obra.setCliente(cliente);
        obra.setEstado(EstadoObra.PENDIENTE);
    }

    @Test
    void testFindAll() {
        List<Obra> obras = Arrays.asList(obra, new Obra());
        when(obraRepository.findAll()).thenReturn(obras);

        List<Obra> result = obraService.findAll();
        assertThat(result).hasSize(2);
        verify(obraRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(obraRepository.findById(1)).thenReturn(Optional.of(obra));

        Optional<Obra> result = obraService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getDireccion()).isEqualTo("Direccion Test");
        verify(obraRepository, times(1)).findById(1);
    }

    @Test
    void testSave() {
        when(obraRepository.save(any(Obra.class))).thenReturn(obra);

        Obra result = obraService.save(obra);
        assertThat(result.getDireccion()).isEqualTo("Direccion Test");
        verify(obraRepository, times(1)).save(obra);
    }

    @Test
    void testUpdate() {
        when(obraRepository.save(any(Obra.class))).thenReturn(obra);

        Obra result = obraService.update(obra);
        assertThat(result.getDireccion()).isEqualTo("Direccion Test");
        verify(obraRepository, times(1)).save(obra);
    }

    @Test
    void testDeleteById() {
        doNothing().when(obraRepository).deleteById(1);

        obraService.deleteById(1);
        verify(obraRepository, times(1)).deleteById(1);
    }

    @Test
    void testEnableObra() {
        obra.setEstado(EstadoObra.PENDIENTE);
        when(obraRepository.findByClienteAndEstado(cliente, EstadoObra.HABILITADA)).thenReturn(Arrays.asList());
        when(obraRepository.save(any(Obra.class))).thenReturn(obra);

        Obra result = obraService.enableObra(obra);
        assertThat(result.getEstado()).isEqualTo(EstadoObra.HABILITADA);
        verify(obraRepository, times(1)).save(obra);
    }

    @Test
    void testFinishObra() {
        obra.setEstado(EstadoObra.HABILITADA);
        Obra obraPendiente = new Obra();
        obraPendiente.setEstado(EstadoObra.PENDIENTE);
        when(obraRepository.save(any(Obra.class))).thenReturn(obra);
        when(obraRepository.findByClienteAndEstado(cliente, EstadoObra.PENDIENTE)).thenReturn(Arrays.asList(obraPendiente));

        Obra result = obraService.finishObra(obra);
        assertThat(result.getEstado()).isEqualTo(EstadoObra.FINALIZADA);
        verify(obraRepository, times(1)).save(obra);
        verify(obraRepository, times(1)).save(obraPendiente);
    }

    @Test
    void testPendingObra() {
        when(obraRepository.save(any(Obra.class))).thenReturn(obra);

        Obra result = obraService.pendingObra(obra);
        assertThat(result.getEstado()).isEqualTo(EstadoObra.PENDIENTE);
        verify(obraRepository, times(1)).save(obra);
    }
}
