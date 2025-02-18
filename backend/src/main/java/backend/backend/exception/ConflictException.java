package backend.backend.exception;


import backend.backend.common.ErrorType;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(ErrorType.CONFLICT_ERROR, message);
    }
}
