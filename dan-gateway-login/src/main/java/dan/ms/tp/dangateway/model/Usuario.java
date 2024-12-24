package dan.ms.tp.dangateway.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    private Long id;

    private String username;
    private String password;

    private RolesUsuario rol;

}
