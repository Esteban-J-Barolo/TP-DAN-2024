package isi.dan.msclientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.msclientes.aop.LogExecutionTime;
import isi.dan.msclientes.exception.AgregarObraNotFoundException;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.servicios.ClienteService;
import isi.dan.msclientes.servicios.ObraService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/obras")
public class ObraController {

    @Autowired
    private ObraService obraService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @LogExecutionTime
    public List<Obra> getAll() {
        return obraService.findAll();
    }

    @GetMapping("/{id}")
    @LogExecutionTime
    public ResponseEntity<Obra> getById(@PathVariable Integer id) {
        Optional<Obra> obra = obraService.findById(id);
        return obra.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{id}")
    @LogExecutionTime
    public ResponseEntity<List<Obra>> getByClienteId(@PathVariable Integer id) {
        Cliente cliente = clienteService.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        List<Obra> obras = obraService.findByCliente(cliente);
        return obras.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(obras);
    }

    @PostMapping
    public Obra create(@RequestBody Obra obra) {
        return obraService.save(obra);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Obra> update(@PathVariable Integer id, @RequestBody Obra obra) {
        if (!obraService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        obra.setId(id);
        return ResponseEntity.ok(obraService.update(obra));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!obraService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        obraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/agregar-cliente/{id}")
    @LogExecutionTime
    public ResponseEntity<Obra> agregarCliente(@PathVariable final Integer clienteId, @RequestBody Obra obra) throws ClienteNotFoundException, AgregarObraNotFoundException {
        return ResponseEntity.ok(obraService.agregarCliente(clienteId, obra));
    }    
    
    @PutMapping("/finalizar/{id}")
    @LogExecutionTime
    public ResponseEntity<Obra> finish(@PathVariable Integer id, @RequestBody Obra obra) {
        if (!obraService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        obra.setId(id);
        return ResponseEntity.ok(obraService.finishObra(obra));
    }
    
    @PutMapping("/habilitar/{id}")
    @LogExecutionTime
    public ResponseEntity<Obra> enable(@PathVariable Integer id, @RequestBody Obra obra) {
        if (!obraService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        obra.setId(id);
        return ResponseEntity.ok(obraService.enableObra(obra));
    }
    
    @PutMapping("/pendiente/{id}")
    @LogExecutionTime
    public ResponseEntity<Obra> pending(@PathVariable Integer id, @RequestBody Obra obra) {
        if (!obraService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        obra.setId(id);
        return ResponseEntity.ok(obraService.pendingObra(obra));
    }
}
