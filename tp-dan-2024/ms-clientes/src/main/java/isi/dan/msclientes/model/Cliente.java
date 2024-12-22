package isi.dan.msclientes.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//BD
@Entity
@Table(name = "MS_CLI_CLIENTE")
//lombok
@Data //getter + setter + RequiredArgsConstructor + ToString + EqualsAndHashCode
public class Cliente {
    
    //BD
    @Id // identifica el atributo como PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //genera el valor de forma automatica por la BD
    private Integer id;

    // validator
    @NotBlank(message = "El nombre es obligatorio") //campo no null
    private String nombre;

    @Column(name="CORREO_ELECTRONICO")
    //validator
    @Email(message = "Email debe ser valido") //verifica que el correo tenga el formato correcto
    @NotBlank(message = "Email es obligatorio")
    private String correoElectronico;
    
    private String cuit;

    @Column(name="MAXIMO_DESCUBIERTO")
    //validator
    @DecimalMin(value = "0.0", message = "El descubierto no puede ser negativo") 
    private BigDecimal maximoDescubierto;
    
    @NotNull(message = "Máximo de obras es obligatorio")
    private Integer maximoObrasActivas;
    
}
