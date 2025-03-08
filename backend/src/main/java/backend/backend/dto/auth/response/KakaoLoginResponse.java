package backend.backend.dto.auth.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponse {
    private String accessToken;
    private String email;
    private String message;
}
