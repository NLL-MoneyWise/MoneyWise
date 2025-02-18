package backend.backend.dto.auth.response;

import backend.backend.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String message;
}
