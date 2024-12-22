package isi.dan.ms.pedidos.modelo;

import java.time.Instant;

import lombok.Data;

@Data
public class HistorialPedido {

    private Instant fecha;
    private EstadoPedido estado;
    private String userEstado;
    private String detalle;

    public HistorialPedido(){
    }

    public HistorialPedido(Instant fecha, EstadoPedido estado){
        this.fecha = fecha;
        this.estado = estado;
    }

}
