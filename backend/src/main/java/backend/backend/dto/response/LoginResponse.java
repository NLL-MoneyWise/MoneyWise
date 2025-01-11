package backend.backend.dto.response;

import backend.backend.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String tokenType;
    private String accessToken;
    private UserInfo userInfo;

    public static LoginResponse of(String accessToken, User user) {
        return LoginResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .userInfo(UserInfo.from(user))
                .build();
    }
    @Getter
    @Builder
    public static class UserInfo {
        private String email;
        private String name;
        private String nickname;

        public static UserInfo from(User user) {
            return UserInfo.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .build();
        }
    }
}
