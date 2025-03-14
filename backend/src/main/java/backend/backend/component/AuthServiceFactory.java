package backend.backend.component;

import backend.backend.dto.auth.request.KakaoLoginRequest;
import backend.backend.dto.auth.request.KakaoSignupRequest;
import backend.backend.dto.auth.request.LocalLoginRequest;
import backend.backend.dto.auth.request.LocalSignupRequest;
import backend.backend.service.AuthService;
import backend.backend.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthServiceFactory {
    //이름이 지정된 빈들을 자동으로 매핑
    private final Map<String, AuthService<?, ?>> authServiceMap;

    //컴파일러 경고를 무시하는 어노테이션
    @SuppressWarnings("unchecked")
    public AuthService<LocalLoginRequest, LocalSignupRequest> getLocalAuthService() {
        return (AuthService<LocalLoginRequest, LocalSignupRequest>) authServiceMap.get("local");
    }

    @SuppressWarnings("unchecked")
    public AuthService<KakaoLoginRequest, KakaoSignupRequest> getKakaoAuthService() {
        return (AuthService<KakaoLoginRequest, KakaoSignupRequest>) authServiceMap.get("kakao");
    }
}
