package backend.backend.exception;

import backend.backend.common.ErrorType;
import lombok.Getter;

@Getter
public class AuthException extends BaseException{
    public AuthException(String message) {
        super(ErrorType.AUTH_ERROR, message);
    }
}
