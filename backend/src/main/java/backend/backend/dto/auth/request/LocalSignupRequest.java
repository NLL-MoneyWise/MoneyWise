package backend.backend.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalSignupRequest implements BaseSignupRequest {
    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상이며, 숫자, 특수문자, 영문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력입니다.")
    @Size(min = 2, message = "이름은 2자 이상이여야 합니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력입니다.")
    @Size(min = 2, message = "닉네임은 2자 이상이여야 합니다.")
    private String nickname;
}
