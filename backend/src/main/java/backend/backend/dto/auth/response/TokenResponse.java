package backend.backend.dto.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String access_token;
    private String message;
}
