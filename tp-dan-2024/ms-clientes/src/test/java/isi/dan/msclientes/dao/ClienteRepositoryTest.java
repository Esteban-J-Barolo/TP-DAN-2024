package isi.dan.msclientes.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import isi.dan.msclientes.model.Cliente;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) //Integra el soporte de Spring TestContext Framework con JUnit 5
@DataJpaTest //indica capa de persistencia y crea  una BD en memoria
@Testcontainers //indica que se van a utilizar contenedores
@ActiveProfiles("db")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //para no reemplazar la configuración en la bd en las pruebas
public class ClienteRepositoryTest {

    Logger log = LoggerFactory.getLogger(ObraRepositoryTest.class);

    @Container //contenedor de la bd para las pruebas
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ClienteRepository clienteRepository;

    @DynamicPropertySource //configurar para utilizar la bd
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @BeforeEach
    void iniciarDatos(){
        Cliente cliente = new Cliente();
        cliente.setNombre("Test cliente");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("23413598249");
        cliente.setMaximoDescubierto(BigDecimal.valueOf(200000));
        cliente.setMaximoObrasActivas(5);
        clienteRepository.save(cliente);
    }

    @BeforeEach
    void borrarDatos(){
        clienteRepository.deleteAll();
    }

    @AfterAll
    static void stopContainer() {
        mysqlContainer.stop();
    }

    @Test
    public void testSaveAndFindById() {
        
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente test");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("23413598249");
        cliente.setMaximoDescubierto(BigDecimal.valueOf(200000));
        cliente.setMaximoObrasActivas(5);

        clienteRepository.save(cliente);

        Optional<Cliente> foundCliente = clienteRepository.findById(cliente.getId());
        assertThat(foundCliente).isPresent();
        assertThat(foundCliente.get().getNombre()).isEqualTo("Cliente test");
    }
    
    @Test
    void testDeleteById() {
        
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente test");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("23413598249");
        cliente.setMaximoDescubierto(BigDecimal.valueOf(200000));
        cliente.setMaximoObrasActivas(5);

        clienteRepository.save(cliente);

         // Verificar que la obra existe
         Optional<Cliente> foundCliente = clienteRepository.findById(cliente.getId());
         assertThat(foundCliente).isPresent();
 
         // Eliminar la obra por ID
         clienteRepository.deleteById(cliente.getId());
 
         // Verificar que la obra ha sido eliminada
         Optional<Cliente> deletedcliente = clienteRepository.findById(cliente.getId());
         assertThat(deletedcliente).isNotPresent();

    }
    
    @Test
    void testFindAll() {
        
        
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente test");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("23413598249");
        cliente.setMaximoDescubierto(BigDecimal.valueOf(200000));
        cliente.setMaximoObrasActivas(5);

        clienteRepository.save(cliente);

        List<Cliente> resultado = clienteRepository.findAll();
        assertThat(resultado.size()).isEqualTo(2);
    }


}