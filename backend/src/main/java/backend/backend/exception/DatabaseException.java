package backend.backend.exception;

import backend.backend.common.ErrorType;

public class DatabaseException extends BaseException {
    public DatabaseException(String message) {
        super(ErrorType.DATABASE_ERROR, message);
    }
}
