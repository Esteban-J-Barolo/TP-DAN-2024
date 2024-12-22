package isi.dan.ms_productos.modelo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
// import java.util.List;

import isi.dan.ms_productos.dto.Provision;

@Entity
@Table(name = "MS_PRD_PRODUCTO")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    private String descripcion;
    @Column(name ="STOCK_ACTUAL")
    private int stockActual = 0;
    @Column(name ="STOCK_MINIMO")
    private int stockMinimo;
    private BigDecimal precio;
    private BigDecimal descuento;

    private String codigo;
    
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public void actualizar(Provision provision){
        this.stockActual += provision.getStockRecibido();
        this.precio = provision.getPrecio();
    }

    public BigDecimal precioPromocion(){
        return this.precio.subtract(descuento);
    }

}
