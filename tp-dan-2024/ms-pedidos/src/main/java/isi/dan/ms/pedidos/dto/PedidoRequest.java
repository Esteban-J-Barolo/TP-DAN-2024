package isi.dan.ms.pedidos.dto;

import java.util.List;

import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.Obra;
import lombok.Data;

@Data
public class PedidoRequest {
    
    private Cliente cliente;
    private Obra obra;
    private List<DetallePedido> detallesPedido;
    private String observacion;

}
