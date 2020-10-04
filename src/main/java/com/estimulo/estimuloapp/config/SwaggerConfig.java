/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Bean
  public Docket produceApi(@Value("${swagger.enabled}") Boolean enabled) {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfoFactory())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.estimulo.estimuloapp.controller"))
        .paths(PathSelectors.any())
        .build()
        .enable(enabled);
  }

  private ApiInfo apiInfoFactory() {
    return new ApiInfoBuilder()
        .title("Estimulo Web Application Service")
        .description("This page will list all estimulo app endpoints.")
        .version("1.0-SNAPSHOT")
        .build();
  }
}
