package com.knu.algo_hive.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Algo-Hive Team Toy Project 명세서")
                .description("[Team Notion 바로가기](https://www.notion.so/1602c5ccabe480a4821bdbf72f003c57)")
                .version("0.0.1");
    }
}
