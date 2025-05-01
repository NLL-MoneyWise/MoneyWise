package backend.backend.dto.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenValidationRequest {
    String access_token;
}
