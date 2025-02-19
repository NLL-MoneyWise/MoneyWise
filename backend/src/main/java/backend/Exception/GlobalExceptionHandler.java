package backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 전역 예외 처리 활성화
public class GlobalExceptionHandler {

    @ExceptionHandler(MemoNotFoundException.class) // MemoNotFoundException 처리
    public ResponseEntity<String> handleMemoNotFoundException(MemoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 상태 반환
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class) // 기타 모든 예외 처리
    public ResponseEntity<String> handleGeneralException(GeneralException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 상태 반환
                .body(e.getMessage());
    }
}
