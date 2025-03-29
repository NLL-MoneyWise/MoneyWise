package backend.backend.exception;

import backend.backend.common.ErrorType;
import backend.backend.exception.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

//RuntimeException을 상속받음으로써 throw나 e.getMessage가 가능하다.
@Getter
public class BaseException extends RuntimeException {
    private final ErrorResponse response;
    private final HttpStatus status;
    public BaseException(ErrorType errorType, String message) {
        //super로 runtime에 메시지를 전달하지 않으면 다른 객체에서 e.getResponse.getMessage()로 사용해야함
        super(message);
        this.status = errorType.getStatus();
        this.response = new ErrorResponse(errorType.getTypeName(), message);
    }
}
