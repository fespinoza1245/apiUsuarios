package cl.fes.apiUsuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiErrorDTO {

    private String message;
    private HttpStatus status;

}
