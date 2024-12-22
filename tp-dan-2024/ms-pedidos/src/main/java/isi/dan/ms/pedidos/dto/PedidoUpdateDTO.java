package isi.dan.ms.pedidos.dto;

import java.math.BigDecimal;

import isi.dan.ms.pedidos.modelo.EstadoPedido;
import lombok.Data;

@Data
public class PedidoUpdateDTO {
    private String id;
    private String observaciones;
    private BigDecimal total;
    private EstadoPedido estado;

}
