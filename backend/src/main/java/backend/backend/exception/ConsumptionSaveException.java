package backend.backend.exception;

import backend.backend.dto.response.ErrorResponse;
import lombok.Getter;

@Getter
public class ConsumptionSaveException extends RuntimeException {
    private final ErrorResponse errorResponse;
    public ConsumptionSaveException (ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
