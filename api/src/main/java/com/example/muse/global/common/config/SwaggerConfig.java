package com.example.muse.global.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final List<String> PROVIDERS = List.of("google", "kakao", "naver");

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("Muse API");

        return new OpenAPI()
                .info(info);
    }

    @Bean
    public OpenApiCustomizer oauth2Customizer() {
        return openApi -> {
            PROVIDERS.forEach(provider -> {
                String path = "/oauth2/authorization/" + provider;
                openApi.getPaths().addPathItem(
                        path,
                        new PathItem().get(new Operation()
                                .addTagsItem("OAuth2 Client")
                                .description(provider + " 인가 요청 엔드포인트")
                                .responses(new ApiResponses()
                                        .addApiResponse("302",
                                                new ApiResponse().description(provider + " OAuth 서버 리다이렉트")
                                        )
                                )
                        )
                );
            });
        };
    }
}