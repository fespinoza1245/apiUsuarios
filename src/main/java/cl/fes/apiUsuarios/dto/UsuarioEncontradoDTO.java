package cl.fes.apiUsuarios.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioEncontradoDTO {

    private String id;
    private String nombre;
    private String email;
    private String created;
    private String estado;
}
