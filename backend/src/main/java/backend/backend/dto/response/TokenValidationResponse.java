package backend.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenValidationResponse {
    private String message;
}
