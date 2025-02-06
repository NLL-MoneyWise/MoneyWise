package backend.backend.exception;

import backend.backend.dto.response.ErrorResponse;
import lombok.Getter;

@Getter
public class LoginException extends RuntimeException{
    private final ErrorResponse errorResponse;
    public LoginException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
