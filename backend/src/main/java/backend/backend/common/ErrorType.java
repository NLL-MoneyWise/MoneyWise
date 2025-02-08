package backend.backend.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR"),      // 500: DB 관련 에러
    NETWORK_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "NETWORK_ERROR"),          // 503: 네트워크 통신 문제
    INVALID_INPUT_ERROR(HttpStatus.BAD_REQUEST, "INVALID_INPUT_ERROR"),      // 400: 잘못된 입력값
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_ERROR"),    // 401: 인증 실패
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "NOT_FOUND_ERROR"),               // 404: 리소스 없음
    CONFLICT_ERROR(HttpStatus.CONFLICT, "CONFLICT_ERROR"),                   // 409: 리소스 충돌
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),  // 500: 서버 내부 오류
    EXTERNAL_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "EXTERNAL_SERVICE_ERROR"), // 503: 외부 서비스 오류
    BAD_GATEWAY_ERROR(HttpStatus.BAD_GATEWAY, "BAD_GATEWAY_ERROR"); //502: 업스트림 서버로 부터 유효하지 않은 응답을 받았을 경우

    private final String typeName;
    private final HttpStatus status;

    ErrorType(HttpStatus status, String typeName) {
        this.typeName = typeName;
        this.status = status;
    }
}
