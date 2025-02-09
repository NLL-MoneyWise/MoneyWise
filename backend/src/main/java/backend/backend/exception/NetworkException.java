package backend.backend.exception;

import backend.backend.common.ErrorType;

public class NetworkException extends BaseException {

    public NetworkException(String message) {
        super(ErrorType.NETWORK_ERROR, message);
    }
}
