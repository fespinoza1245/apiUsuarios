package cl.fes.apiUsuarios.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    @Column(name = "nombre_apellido")
    @NotNull(message = "El nombre no puede ser nulo")
    private String name;
    @Column(name = "email", unique = true)
    @NotNull(message = "El email no puede ser nulo")
    private String email;
    @Column(name = "password")
    @NotNull(message = "El password no puede ser nulo")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Telefono> phones;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime created;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime modified;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime last_login;
    private String token;
    private boolean isActive;

}
