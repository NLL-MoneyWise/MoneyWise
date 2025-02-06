package backend.backend.common;

public enum ErrorType {
    DATABASE_ERROR, //데이터베이스 연산 중 발생하는 에러
    NETWORK_ERROR, //네트워크 통신과정에서 발생하는 에러
    INVALID_INPUT_ERROR, //사용자가 잘못된 입력값을 입력 했을 경우
    AUTHENTICATION_ERROR //인증 과정에서 발생하는 에러 JWT TOKEN 등
}
