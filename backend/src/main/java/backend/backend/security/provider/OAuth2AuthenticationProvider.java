package backend.backend.security.provider;

import backend.backend.domain.User;
import backend.backend.dto.auth.response.LoginResponse;
import backend.backend.repository.UserRepository;
import backend.backend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class OAuth2AuthenticationProvider implements AuthenticationProvider {
    protected final UserRepository userRepository;
    protected final JwtUtils jwtUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String providerType = authentication.getName();

        if(!getProviderType().equals(providerType)) {
            return null; // 처리할 수 없으면 null 반환 (다음 provider로 넘어감)
        }

        String code = authentication.getCredentials().toString();

        User user = authenticationUser(code);

        String access_token = jwtUtils.generateAccessToken(user.getEmail(), user.getName(), user.getNickname());

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        LoginResponse details = LoginResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .access_token(access_token)
                .build();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        authToken.setDetails(details);

        return authToken;
    }

    protected abstract User authenticationUser(String code);
    public abstract String getProviderType();

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
