package backend.backend.service;

import backend.backend.dto.auth.request.LocalLoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthServiceTest {
    @Autowired
    private LocalAuthService localAuthService;
    @Test
    @DisplayName("실제 로그인 테스트")
    void loginSuccess() {
        String email = "test@naver.com";
        String password = "testPassword@1231";

        LocalLoginRequest request = LocalLoginRequest.builder().email(email).password(password).build();

        String accessToken = localAuthService.login(request);

        System.out.println(accessToken);

        Assertions.assertNotNull(accessToken);
    }
}
