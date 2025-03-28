package backend.backend.dto.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocalLoginResponse {
    private String accessToken;
    private String message;
}
