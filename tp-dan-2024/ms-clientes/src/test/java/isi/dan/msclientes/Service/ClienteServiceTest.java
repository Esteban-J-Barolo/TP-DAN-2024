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

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.servicios.ClienteService;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Test cliente");
    }

    @Test
    void testFindAll() {
        List<Cliente> clientes = Arrays.asList(cliente, new Cliente());
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = clienteService.findAll();
        assertThat(result).hasSize(2);
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Test cliente");
        verify(clienteRepository, times(1)).findById(1);
    }

    @Test
    void testSave() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.save(cliente);
        assertThat(result.getNombre()).isEqualTo("Test cliente");
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testUpdate() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.update(cliente);
        assertThat(result.getNombre()).isEqualTo("Test cliente");
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testDeleteById() {
        doNothing().when(clienteRepository).deleteById(1);

        clienteService.deleteById(1);
        verify(clienteRepository, times(1)).deleteById(1);
    }
}
