package isi.dan.msclientes.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.dao.UsuarioRepository;
import isi.dan.msclientes.enums.EstadoObra;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.model.Usuario;

import java.math.BigDecimal;
import java.util.stream.IntStream;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {

        obraRepository.deleteAll();
        usuarioRepository.deleteAll();
        clienteRepository.deleteAll();

        // Crear y guardar clientes
        IntStream.range(1, 11).forEach(i -> {
            Cliente cliente = new Cliente();
            cliente.setNombre("Cliente " + i);
            cliente.setCorreoElectronico("cliente" + i + "@example.com");
            cliente.setCuit("20-" + (10000000 + i) + "-9");
            cliente.setMaximoDescubierto(BigDecimal.valueOf(10000 + i * 1000));
            cliente.setMaximoObrasActivas(i % 5 + 1);
            clienteRepository.save(cliente);

            // Crear y guardar obras para cada cliente
            IntStream.range(1, 4).forEach(j -> {
                Obra obra = new Obra();
                obra.setDireccion("Dirección " + j + " del cliente " + i);
                obra.setEsRemodelacion(j % 2 == 0);
                obra.setLat(-34.6f + i * 0.01f);
                obra.setLng(-58.4f + j * 0.01f);
                obra.setPresupuesto(BigDecimal.valueOf(5000 + j * 1000));
                obra.setEstado(EstadoObra.HABILITADA);
                obra.setCliente(cliente);
                obraRepository.save(obra);
            });

            // Crear y guardar usuarios para cada cliente
            IntStream.range(1, 3).forEach(k -> {
                Usuario usuario = new Usuario();
                usuario.setNombre("Usuario " + k + " del cliente " + i);
                usuario.setApellido("Apellido " + k);
                usuario.setDni(String.valueOf(10000000 + i * 10 + k));
                usuario.setCorreoElectronico("usuario" + k + ".cliente" + i + "@example.com");
                usuario.setCliente(cliente);
                usuarioRepository.save(usuario);
            });
        });

        System.out.println("Datos de ejemplo cargados en la base de datos.");
    }
}
