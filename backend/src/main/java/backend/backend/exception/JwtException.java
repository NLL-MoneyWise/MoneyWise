package backend.backend.exception;

import backend.backend.dto.response.ErrorResponse;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final ErrorResponse errorResponse;
    public JwtException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
