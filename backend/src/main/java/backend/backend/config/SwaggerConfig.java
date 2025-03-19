package backend.backend.config;

import backend.backend.exception.response.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class SwaggerConfig {
    private static final String JWT_SECURITY_SCHEME_NAME = "JWT";
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityRequirement securityRequirement = createSecurityRequirement();
        Components components = createSecurityComponents();

        //components/reponses에 401커스텀 에러를 등록
        components.addResponses("401", new ApiResponse()
                .description("인증에 실패했습니다.")
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(createErrorResponseSchema()))));

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
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

    private Schema createErrorResponseSchema() {
        return new Schema<ErrorResponse>()
                .type("object")
                .example(Map.of(
                        "typeName", "AUTH_ERROR",
                        "message", "인증에 실패했습니다."
                ));
    }

    @Bean
    public OperationCustomizer globalSecurityResponseCustomizer() {
        return (operation, handlerMethod) -> {
            if (operation.getSecurity() != null && !operation.getSecurity().isEmpty()) {
                //components/responses안에 내가 정의한 401에러를 참조하여 조건을 충족하면 자동으로 에러 문서 등록
                operation.getResponses().addApiResponse("401", new ApiResponse().$ref("#/components/responses/401"));
            }

            return operation;
        };
    }
}
