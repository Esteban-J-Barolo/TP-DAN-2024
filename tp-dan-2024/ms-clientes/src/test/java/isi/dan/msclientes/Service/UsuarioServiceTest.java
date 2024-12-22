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

import isi.dan.msclientes.dao.UsuarioRepository;
import isi.dan.msclientes.model.Usuario;
import isi.dan.msclientes.servicios.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Usuario Test");
        usuario.setApellido("Apellido Test");
        usuario.setCorreoElectronico("test@example.com");
        usuario.setDni("12345678");
    }

    @Test
    void testFindAll() {
        List<Usuario> usuarios = Arrays.asList(usuario, new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.findAll();
        assertThat(result).hasSize(2);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Usuario Test");
        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    void testSave() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.save(usuario);
        assertThat(result.getNombre()).isEqualTo("Usuario Test");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testUpdate() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.update(usuario);
        assertThat(result.getNombre()).isEqualTo("Usuario Test");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1);

        usuarioService.deleteById(1);
        verify(usuarioRepository, times(1)).deleteById(1);
    }
}
