package backend.backend.security.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JWToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
