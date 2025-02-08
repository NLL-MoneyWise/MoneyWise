package backend.backend.exception;

import backend.backend.common.ErrorType;

public class ExternalServiceException extends BaseException {

    public ExternalServiceException(String message) {
        super(ErrorType.EXTERNAL_SERVICE_ERROR, message);
    }
}
