package backend.backend.exception;

import backend.backend.common.ErrorType;
import lombok.Getter;

@Getter
public class AuthenticationException extends BaseException{
    public AuthenticationException(String message) {
        super(ErrorType.AUTHENTICATION_ERROR, message);
    }
}
