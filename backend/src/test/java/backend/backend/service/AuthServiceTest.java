package backend.backend.service;

import backend.backend.dto.auth.request.LocalLoginRequest;
import backend.backend.dto.auth.response.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Test
    @DisplayName("실제 로그인 테스트")
    void loginSuccess() {
        String email = "test@naver.com";
        String password = "testPassword@1231";

        LocalLoginRequest request = LocalLoginRequest.builder().email(email).password(password).build();

        LoginResponse response = authService.login("local", request);

        System.out.println(response.getAccess_token());

        Assertions.assertNotNull(response.getAccess_token());
    }
}
