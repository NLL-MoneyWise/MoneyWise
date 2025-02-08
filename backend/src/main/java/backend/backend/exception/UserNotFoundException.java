package backend.backend.exception;

import backend.backend.common.ErrorType;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(ErrorType.NOT_FOUND_ERROR, message);
    }
}
