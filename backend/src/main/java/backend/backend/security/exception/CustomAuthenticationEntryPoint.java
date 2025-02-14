package backend.backend.security.exception;

import backend.backend.common.ErrorType;
import backend.backend.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 기존 에러 응답 구조를 그대로 활용
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorType.AUTH_ERROR.getTypeName(),    // "AUTH_ERROR"
                "인증에 실패했습니다."
        );

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorType.AUTH_ERROR.getStatus().value());  // .value는 401을 반환

        objectMapper.writeValue(response.getOutputStream(), errorResponse); //객체를 json으로 변환
    }
}