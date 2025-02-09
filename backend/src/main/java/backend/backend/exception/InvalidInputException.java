package backend.backend.exception;

import backend.backend.common.ErrorType;

public class InvalidInputException extends BaseException {
    public InvalidInputException(String message) {
        super(ErrorType.INVALID_INPUT_ERROR, message);
    }
}
