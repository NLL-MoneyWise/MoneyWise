package backend.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String JWT_SECURITY_SCHEME_NAME = "JWT";
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityRequirement securityRequirement = createSecurityRequirement();
        Components components = createSecurityComponents();

        return new OpenAPI().info(apiInfo()).addSecurityItem(securityRequirement)
                .components(components);
    }

    private Info apiInfo() {
        return new Info()
                .title("RESTful API 문서")
                .description("JWT인증을 사용하는 REST API 문서입니다.")
                .version("1.0.0")
                .contact(new Contact().name("백엔드 개발").email("jionu102@naver.com"));
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList(JWT_SECURITY_SCHEME_NAME);
    }

    private Components createSecurityComponents() {
        SecurityScheme jwtSecurityScheme = new SecurityScheme()
                .name(JWT_SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("JWT 토큰을 입력해주세요.");

        return new Components().addSecuritySchemes(JWT_SECURITY_SCHEME_NAME, jwtSecurityScheme);
    }
}
