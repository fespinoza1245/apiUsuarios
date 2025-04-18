package cl.fes.apiUsuarios.exceptions;

import org.springframework.http.HttpStatus;

public class ApiError extends RuntimeException {
    private static final long serialVersionUID = 1L;
	private final HttpStatus status;

    public ApiError(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
