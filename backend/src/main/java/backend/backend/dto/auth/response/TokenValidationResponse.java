package backend.backend.dto.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenValidationResponse {
    private String message;
}
