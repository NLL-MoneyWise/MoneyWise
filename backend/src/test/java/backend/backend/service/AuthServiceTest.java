package backend.backend.service;

import backend.backend.dto.auth.request.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Test
    @DisplayName("실제 로그인 테스트")
    void loginSuccess() {
        String email = "test@naver.com";
        String password = "testPassword@1231";

        LoginRequest request = LoginRequest.builder().email(email).password(password).build();

        String accessToken = authService.login(request);

        System.out.println(accessToken);

        Assertions.assertNotNull(accessToken);
    }
}
