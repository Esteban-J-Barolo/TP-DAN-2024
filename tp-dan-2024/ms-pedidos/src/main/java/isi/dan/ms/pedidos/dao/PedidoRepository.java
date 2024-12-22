package isi.dan.ms.pedidos.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String> {

    List<Pedido> findByClienteId(Integer clienteId);

    // @Query(value =  "{ " +
    //                 "$and: [" +
    //                 "   ?#{ [0] != null ? { numeroPedido: ?0 } : {} }," +
    //                 "   ?#{ [1] != null ? { 'cliente.id': ?1 } : {} }," +
    //                 "   ?#{ [2] != null ? { estado: ?2 } : {} }" +
    //                 "]}")
    Page<Pedido> findByNumeroPedido(Integer numeroPedido, Pageable pageable);
    Page<Pedido> findByCliente_Id(Integer clienteId, Pageable pageable);
    Page<Pedido> findByEstado(EstadoPedido estado, Pageable pageable);
    Page<Pedido> findByNumeroPedidoAndCliente_Id(Integer numeroPedido, Integer clienteId, Pageable pageable);
    Page<Pedido> findByNumeroPedidoAndCliente_IdAndEstado(
        Integer numeroPedido, 
        Integer clienteId, 
        EstadoPedido estado, 
        Pageable pageable
    );
    default Page<Pedido> buscarPorCriterios(
                                    Integer numeroPedido,
                                    Integer clienteId, 
                                    EstadoPedido estado,
                                    Pageable pageable){
            if (numeroPedido == null && clienteId == null && estado == null) {
                return findAll(pageable);
            }

            if (numeroPedido != null && clienteId == null && estado == null) {
                return findByNumeroPedido(numeroPedido, pageable);
            }
    
            if (numeroPedido == null && clienteId != null && estado == null) {
                return findByCliente_Id(clienteId, pageable);
            }
    
            if (numeroPedido == null && clienteId == null && estado != null) {
                return findByEstado(estado, pageable);
            }

            if (numeroPedido != null && clienteId != null && estado == null) {
                return findByNumeroPedidoAndCliente_Id(numeroPedido, clienteId, pageable);
            }

            return findByNumeroPedidoAndCliente_IdAndEstado(numeroPedido, clienteId, estado, pageable);

    }
    
}

