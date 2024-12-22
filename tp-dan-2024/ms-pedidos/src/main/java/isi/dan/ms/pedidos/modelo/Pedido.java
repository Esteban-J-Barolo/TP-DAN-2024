package isi.dan.ms.pedidos.modelo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pedidos")
@Data
public class Pedido {
    @Id
    private String id;
    private Instant fecha;
    private Integer numeroPedido;
    private String usuario;
    private String observaciones;

    private Cliente cliente;
    private Obra obra;
    private BigDecimal total;
    private EstadoPedido estado;
    private List<HistorialPedido> estados = new ArrayList<HistorialPedido>();

    @Field("detalle")
    private List<DetallePedido> detalle;

    public Pedido() {
        this.fecha = Instant.now();
    }

    public void agregarEstado(HistorialPedido historialPedido){
        this.estados.add(historialPedido);
    }

}

