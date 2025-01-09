package backend.backend.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JWToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
