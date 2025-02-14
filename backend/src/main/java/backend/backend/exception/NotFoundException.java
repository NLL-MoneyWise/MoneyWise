package backend.backend.exception;

import backend.backend.common.ErrorType;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(ErrorType.NOT_FOUND_ERROR, message);
    }
}
