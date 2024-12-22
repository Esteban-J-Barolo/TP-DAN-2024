package isi.dan.msclientes.dao;
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

import isi.dan.msclientes.enums.EstadoObra;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) //Integra el soporte de Spring TestContext Framework con JUnit 5
@DataJpaTest //indica capa de persistencia y crea  una BD en memoria
@Testcontainers //indica que se van a utilizar contenedores
@ActiveProfiles("db")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //para no reemplazar la configuración en la bd en las pruebas
public class ObraRepositoryTest {

    Logger log = LoggerFactory.getLogger(ObraRepositoryTest.class);

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ObraRepository obraRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @BeforeEach
    void borrarDatos(){
        obraRepository.deleteAll();
    }

    @BeforeEach
    void iniciarDatos(){
        Obra obra = new Obra();
        obra.setDireccion("Test Obra 999");
        obra.setPresupuesto(BigDecimal.valueOf(100000));
        obra.setEstado(EstadoObra.PENDIENTE);
        obraRepository.save(obra);
        
        cliente = new Cliente();
        cliente.setNombre("cli 1");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("23413598249");
        cliente.setMaximoDescubierto(BigDecimal.valueOf(200000));
        cliente.setMaximoObrasActivas(5);
        clienteRepository.save(cliente);
    }

    @AfterAll
    static void stopContainer() {
        mysqlContainer.stop();
    }

    @Test
    void testSaveAndFindById() {
        Obra obra = new Obra();
        obra.setDireccion("Test Obra");
        obra.setPresupuesto(BigDecimal.valueOf(32000));
        obraRepository.save(obra);

        Optional<Obra> foundObra = obraRepository.findById(obra.getId());
        log.info("ENCONTRE: {} ",foundObra);
        assertThat(foundObra).isPresent();
        assertThat(foundObra.get().getDireccion()).isEqualTo("Test Obra");
    }

    @Test
    void testFindByClienteAndEstado() {

        Obra obra = new Obra();
        obra.setDireccion("Test Obra");
        obra.setPresupuesto(BigDecimal.valueOf(123200));
        obra.setCliente(cliente);
        obra.setEstado(EstadoObra.PENDIENTE);
        obraRepository.save(obra);

        List<Obra> resultado = obraRepository.findByClienteAndEstado(cliente, EstadoObra.PENDIENTE);
        log.info("ENCONTRE: {} ", resultado);
        assertThat(resultado.size()).isEqualTo(1);
        assertThat(resultado.get(0).getPresupuesto()).isGreaterThan(BigDecimal.valueOf(30000));
    }
    
    @Test
    void testDeleteById() {
        
        Obra obra = new Obra();
        obra.setDireccion("Test Obra");
        obra.setPresupuesto(BigDecimal.valueOf(123200));
        obra.setCliente(cliente);
        obra.setEstado(EstadoObra.PENDIENTE);
        obraRepository.save(obra);

         // Verificar que la obra existe
         Optional<Obra> foundObra = obraRepository.findById(obra.getId());
         log.info("ENCONTRE: {} ", foundObra);
         assertThat(foundObra).isPresent();
 
         // Eliminar la obra por ID
         obraRepository.deleteById(obra.getId());
 
         // Verificar que la obra ha sido eliminada
         Optional<Obra> deletedObra = obraRepository.findById(obra.getId());
         log.info("ENCONTRE: {} ", foundObra);
         assertThat(deletedObra).isNotPresent();

    }
    
    @Test
    void testFindAll() {
        
        Obra obra = new Obra();
        obra.setDireccion("Test Obra");
        obra.setPresupuesto(BigDecimal.valueOf(12300));
        obraRepository.save(obra);

        List<Obra> resultado = obraRepository.findAll();
        log.info("ENCONTRE: {} ", resultado);
        assertThat(resultado.size()).isEqualTo(2);
    }

}