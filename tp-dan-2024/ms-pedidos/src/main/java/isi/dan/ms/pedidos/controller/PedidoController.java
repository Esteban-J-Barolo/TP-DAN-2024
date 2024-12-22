package isi.dan.ms.pedidos.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms.pedidos.dto.PedidoRequest;
import isi.dan.ms.pedidos.dto.PedidoUpdateDTO;
import isi.dan.ms.pedidos.exception.PedidoNotFoundException;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.servicio.PedidoService;

import java.util.List;


@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody PedidoRequest pedidoRequest) {
        Pedido savedPedido = pedidoService.createPedido(
            pedidoRequest.getCliente(),
            pedidoRequest.getObra(),
            pedidoRequest.getDetallesPedido(),
            pedidoRequest.getObservacion()
        );
        return ResponseEntity.ok(savedPedido);
    }
    // private ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
    //     Pedido savedPedido = pedidoService.savePedido(pedido);
    //     return ResponseEntity.ok(savedPedido);
    // }

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable final String id) throws PedidoNotFoundException {
        Pedido pedido = pedidoService.getPedidoById(id);
        return pedido != null ? ResponseEntity.ok(pedido) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable String id) {
        pedidoService.deletePedido(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/actualizar")
    public ResponseEntity<Pedido> updatePedido(@RequestBody PedidoUpdateDTO pedido) throws PedidoNotFoundException {
        return ResponseEntity.ok(pedidoService.updatePedido(pedido));
    }

    @GetMapping("/estado/{id}")
    public ResponseEntity<EstadoPedido> getEstadoById(@PathVariable final String id) throws PedidoNotFoundException {
        Pedido pedido = pedidoService.getPedidoById(id);
        return ResponseEntity.ok(pedido.getEstado());
    }
    
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Pedido>> getPedidosDeClienteById(@PathVariable final Integer id) {
        List<Pedido> pedidos = pedidoService.getPedidosDeCliente(id);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Void> putCencelarPedido(@PathVariable final String id)  throws PedidoNotFoundException {
        Pedido pedido = pedidoService.getPedidoById(id);
        pedidoService.cancelarPedido(pedido);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Pedido>> buscarPedidosPorCriterios(
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Page<Pedido> pedidos = pedidoService.buscarPedidos(numero, clienteId, estado, page, size);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/estados-pedido")
    public ResponseEntity<EstadoPedido[]> getEstadosPedido(){
        return ResponseEntity.ok(EstadoPedido.values());
    }
    
}

