package backend.backend.exception;

import backend.backend.common.ErrorType;

public class APIException extends BaseException {

    public APIException(String message) {
        super(ErrorType.API_ERROR, message);
    }
}
