package cl.fes.apiUsuarios.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioCreatedDTO {
    private Long id;
    private String name;
    private String email;
    private List<TelefonoDTO> phones;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime last_login;
    private String token;
    private boolean isActive;
}
