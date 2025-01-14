package backend.backend.controller;

import backend.backend.domain.User;
import backend.backend.dto.response.LoginResponse;
import backend.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    //UserService추가 및 /me API생성
    @GetMapping("/me")
    //프론트에서 보낸 authorization요청을 authentication으로 자동 치환, security 에서 자동으로 처리함.
    public ResponseEntity<LoginResponse.UserInfo> getMyInfo(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        return ResponseEntity.ok(LoginResponse.UserInfo.from(user));
    }
}
