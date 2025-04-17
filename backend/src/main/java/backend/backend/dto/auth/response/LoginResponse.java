package backend.backend.dto.auth.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    String email;
    String name;
    String nickname;
    String access_token;
    String message;
}
