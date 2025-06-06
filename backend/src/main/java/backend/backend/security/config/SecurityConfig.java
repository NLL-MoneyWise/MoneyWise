package backend.backend.security.config;

import backend.backend.security.exception.CustomAuthenticationEntryPoint;
import backend.backend.security.jwt.JwtAuthenticationFilter;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.security.provider.KakaoAuthenticationProvider;
import backend.backend.security.provider.LocalAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtUtils jwtUtils;
    private final LocalAuthenticationProvider localAuthenticationProvider;
    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) //csrf 보호 기능 비활성화
                .sessionManagement(session -> session //세션을 사용하지 않게 설정
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //스프링 security의 인증 필터 앞에 jwtAuthenticationFilter추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                // /api/auth/* 에는 누구나 인증없이 접근 가능 그 외 모든 요청은 인증되어야 접근가능함
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/signup",
                                "/healthcheck",
                                "/auth/login/**",
                                "/auth/validate",
                                "/auth/refresh",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.authenticationProvider(localAuthenticationProvider);
        authManagerBuilder.authenticationProvider(kakaoAuthenticationProvider);
        authManagerBuilder.parentAuthenticationManager(null);
        return authManagerBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 Origin (프론트 주소만 열 수도 있음, 예: "http://localhost:3000")
        config.addAllowedOrigin("http://localhost:3000"); // 로컬 프론트엔드
        config.addAllowedOrigin("http://localhost:5501"); // Live Server 등
        config.addAllowedOrigin("http://localhost:5500"); // Live Server 등
        config.addAllowedOrigin("https://money-wise-delta.vercel.app");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
