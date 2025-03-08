package backend.backend.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoApiResponse {
    private Long id;
    private Properties properties;

    @Getter
    @Setter
    public static class Properties {
        @JsonProperty("nickname")
        private String nickName;
    }
}
