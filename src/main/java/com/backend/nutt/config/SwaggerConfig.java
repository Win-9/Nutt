package com.backend.nutt.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("Nutt Api 명세")
                .version("1.0.0")
                .description("4학년 캡스톤 디자인 팀 Nutt 프로젝트");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
