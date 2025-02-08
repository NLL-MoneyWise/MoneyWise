package backend.backend.exception;

import backend.backend.common.ErrorType;

public class InternalServerException extends BaseException {

    public InternalServerException(String message) {
        super(ErrorType.INTERNAL_SERVER_ERROR, message);
    }
}
