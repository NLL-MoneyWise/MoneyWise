package backend.backend.exception;

import backend.backend.common.ErrorType;

public class BadGateWayException extends BaseException {

    public BadGateWayException(String message) {
        super(ErrorType.BAD_GATEWAY_ERROR, message);
    }
}
