package backend.backend.dto.auth.request;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoSignupRequest implements BaseSignupRequest{
    private String kakaoId;
    private String email;
    private String name;
    private String nickName;
}
