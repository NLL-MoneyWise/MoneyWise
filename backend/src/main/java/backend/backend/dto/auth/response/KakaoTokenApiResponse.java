package backend.backend.dto.auth.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokenApiResponse {
    private String access_token;
}
