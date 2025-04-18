package cl.fes.apiUsuarios.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioListDTO {

    private String id;
    private String nombre;
    private String email;
    private List<TelefonoDTO> phones;

}
