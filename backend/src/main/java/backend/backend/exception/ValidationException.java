package backend.backend.exception;

import backend.backend.common.ErrorType;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(ErrorType.VALIDATION_ERROR, message);
    }
}
