package com.spaceout.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
      @Bean
      public OpenAPI springShopOpenAPI() {
          return new OpenAPI()
                  .info(new Info().title("SpaceOut REST API")
                  .description("SpaceOut REST API")
                  .version("v0.0.1")
                  .license(new License().name("SpaceOut").url("http://www.spaceout.com")));
      }
}