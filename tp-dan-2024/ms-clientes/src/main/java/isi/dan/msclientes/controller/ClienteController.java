package isi.dan.msclientes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import isi.dan.msclientes.aop.LogExecutionTime;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.model.Usuario;
import isi.dan.msclientes.servicios.ClienteService;
import isi.dan.msclientes.servicios.ObraService;
import isi.dan.msclientes.servicios.UsuarioService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController //Controller + ResponseBody + mapea con JSON
@RequestMapping("/api/clientes")
public class ClienteController {

    Logger log = LoggerFactory.getLogger(ClienteController.class);

    @Value("${dan.clientes.instancia}")
    private String instancia;

    @Autowired //proporcionar una instancia de ClienteService
    private ClienteService clienteService;
    
    @Autowired
    private ObraService obraService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping //solicitud GET a la URL
    @LogExecutionTime //Anotación personalizada para registrar por el aspecto LoggingAspect
    public List<Cliente> getAll() {
        return clienteService.findAll();
    }
    
    @GetMapping("/echo")
    @LogExecutionTime
    public String getEcho() {
        log.debug("Recibiendo un echo ----- {}",instancia);
        return Instant.now()+" - "+instancia;
    }

    @GetMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Cliente> getById(@PathVariable Integer id)  throws ClienteNotFoundException {
        Optional<Cliente> cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente.orElseThrow(()-> new ClienteNotFoundException("Cliente "+id+" no encontrado")));
    }

    @PostMapping
    @LogExecutionTime
    public Cliente create(@RequestBody @Validated Cliente cliente) { //@Validated pasa antes por la api de validación
        return clienteService.save(cliente);
    }

    @PutMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Cliente> update(@PathVariable final Integer id, @RequestBody Cliente cliente) throws ClienteNotFoundException {
        if (!clienteService.findById(id).isPresent()) {
            throw new ClienteNotFoundException("Cliente "+id+" no encontrado");
        }
        cliente.setId(id);
        return ResponseEntity.ok(clienteService.update(cliente));
    }

    @DeleteMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws ClienteNotFoundException {
        if (!clienteService.findById(id).isPresent()) {
                throw new ClienteNotFoundException("Cliente "+id+" no encontrado para borrar");
            }

        Cliente cliente = clienteService.findById(id)
            .orElseThrow(() -> new ClienteNotFoundException("Cliente " + id + " no encontrado para borrar"));

        List<Obra> obras = obraService.findByCliente(cliente);
        obras.forEach(obra -> obraService.deleteById(obra.getId()));

        List<Usuario> usuarios = usuarioService.findByCliente(cliente);
        usuarios.forEach(usuario -> usuarioService.deleteById(usuario.getId()));

        clienteService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Cliente>> buscarClientesPorNombre(@RequestParam String nombre, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Page<Cliente> clientes = clienteService.findByNombreContaining(nombre, page, size);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}/verificar-monto")
    public ResponseEntity<Boolean> verifiacarMonto(@PathVariable Integer id, @RequestParam BigDecimal monto) throws ClienteNotFoundException {
        Cliente cliente = clienteService.findById(id)
                                .orElseThrow(() -> new ClienteNotFoundException("Cliente " + id + " no encontrado para borrar"));
        if(cliente.getMaximoDescubierto().compareTo(monto) == -1) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }
}

