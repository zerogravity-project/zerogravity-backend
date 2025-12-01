package com.zerogravity.myapp.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zerogravity.myapp.common.security.AuthUserId;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;

@Configuration
public class SwaggerConfig {
      @Bean
      public OpenAPI springShopOpenAPI() {
          String securitySchemeName = "bearerAuth";
          return new OpenAPI()
                  .info(new Info().title("ZeroGravity REST API")
                  .description("ZeroGravity REST API")
                  .version("v0.0.1")
                  .license(new License().name("ZeroGravity").url("http://www.zerogravity.com")))
                  .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                  .components(new Components()
                          .addSecuritySchemes(securitySchemeName,
                                  new SecurityScheme()
                                          .name(securitySchemeName)
                                          .type(SecurityScheme.Type.HTTP)
                                          .scheme("bearer")
                                          .bearerFormat("JWT")));
      }

      /**
       * Hide @AuthUserId parameters from Swagger UI
       * These parameters are automatically extracted from JWT token
       */
      @Bean
      public OperationCustomizer customizeOperation() {
          return (operation, handlerMethod) -> {
              // Remove parameters with @AuthUserId annotation from Swagger
              if (operation.getParameters() != null) {
                  operation.getParameters().removeIf(parameter ->
                      java.util.Arrays.stream(handlerMethod.getMethodParameters())
                          .anyMatch(param -> param.hasParameterAnnotation(AuthUserId.class)
                              && param.getParameterName() != null
                              && param.getParameterName().equals(parameter.getName()))
                  );
              }
              return operation;
          };
      }
}