package isi.dan.ms.pedidos.modelo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Obra {

    private Integer id;
    
    private String direccion;
    private Boolean esRemodelacion;
    private float lat;
    private float lng;
    private Cliente cliente;
    private BigDecimal presupuesto;
    private EstadoObra estado = EstadoObra.PENDIENTE;

}
