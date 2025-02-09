package backend.backend.exception;

import backend.backend.common.ErrorType;
import backend.backend.dto.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorResponse response;
    private final HttpStatus status;
    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.status = errorType.getStatus();
        this.response = new ErrorResponse(errorType.getTypeName(), message);
    }
}
