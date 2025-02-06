package backend.backend.exception;

import backend.backend.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //401
                .body(e.getErrorResponse());  // 여기서 getMessage() 사용
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(LoginException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //401
                .body(e.getErrorResponse());
    }

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<String> handleSignupException(SignupException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    //@valid 검증 실패시 MethodArgumentNotValidException이 발생하고 이에따른 에러 처리임
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        for(ObjectError error : e.getAllErrors()) {
            String fieldName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(PresignedException.class)
    public ResponseEntity<String> handlePresignedException(PresignedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(OpenAiApiException.class)
    public ResponseEntity<String> handleOpenAiApiException(OpenAiApiException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(e.getMessage());
    }

    @ExceptionHandler(ConsumptionSaveException.class)
    public ResponseEntity<ErrorResponse> handleConsumptionSaveException(ConsumptionSaveException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getErrorResponse());
    }
}
