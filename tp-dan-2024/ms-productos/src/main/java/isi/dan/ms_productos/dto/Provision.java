package isi.dan.ms_productos.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Provision {

    // private Long idproducto;
    private int stockRecibido;
    private BigDecimal precio;

}
