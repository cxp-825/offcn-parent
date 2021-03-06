package com.offcn.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class AppSwaggerConfig {

    @Bean
    public Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.order.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title("七易众筹-订单平台接口文档")
                .description("提供订单模块的文档说明")
                .version("1.0")
                .build();
    }
}
