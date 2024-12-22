package isi.dan.msclientes.dao;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import isi.dan.msclientes.model.Usuario;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("db")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {
    
    @Container //contenedor de la bd para las pruebas
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UsuarioRepository usuarioRepository;

    @DynamicPropertySource //configurar para utilizar la bd
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @BeforeEach
    void iniciarDatos(){
        Usuario usuario = new Usuario();
        usuario.setNombre("Test usuario");
        usuario.setCorreoElectronico("test@usuario.com");
        usuario.setDni("41359829");
        usuario.setApellido("Apellido test");
        usuarioRepository.save(usuario);
    }

    @BeforeEach
    void borrarDatos(){
        usuarioRepository.deleteAll();
    }

    @AfterAll
    static void stopContainer() {
        mysqlContainer.stop();
    }

    @Test
    public void testSaveAndFindById() {
        
        Usuario usuario = new Usuario();
        usuario.setNombre("Test usuario 2");
        usuario.setCorreoElectronico("test2@usuario.com");
        usuario.setDni("41345629");
        usuario.setApellido("Apellido test 2");

        usuarioRepository.save(usuario);

        Optional<Usuario> foundUsuario = usuarioRepository.findById(usuario.getId());
        assertThat(foundUsuario).isPresent();
        assertThat(foundUsuario.get().getNombre()).isEqualTo("Test usuario 2");
    }
    
    @Test
    void testDeleteById() {
        
        Usuario usuario = new Usuario();
        usuario.setNombre("Test usuario 2");
        usuario.setCorreoElectronico("test2@usuario.com");
        usuario.setDni("41345629");
        usuario.setApellido("Apellido test 2");

        usuarioRepository.save(usuario);

         // Verificar que la obra existe
         Optional<Usuario> foundusuario = usuarioRepository.findById(usuario.getId());
         assertThat(foundusuario).isPresent();
 
         // Eliminar la obra por ID
         usuarioRepository.deleteById(usuario.getId());
 
         // Verificar que la obra ha sido eliminada
         Optional<Usuario> deletedusuario = usuarioRepository.findById(usuario.getId());
         assertThat(deletedusuario).isNotPresent();

    }
    
    @Test
    void testFindAll() {
        
        Usuario usuario = new Usuario();
        usuario.setNombre("Test usuario 2");
        usuario.setCorreoElectronico("test2@usuario.com");
        usuario.setDni("41345629");
        usuario.setApellido("Apellido test 2");

        usuarioRepository.save(usuario);

        List<Usuario> resultado = usuarioRepository.findAll();
        assertThat(resultado.size()).isEqualTo(2);
    }
}
