package backend.backend.dto.auth.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class KakaoSignupRequest implements BaseSignupRequest{
    private String code;
    private String email;
    private String name;
    private String nickName;
}
