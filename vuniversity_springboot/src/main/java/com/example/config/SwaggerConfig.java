package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .groupName("tju")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.controller"))  // 重要配置要扫描的接口（如果不配置，会扫描所有）
                .build();
    }

    //配置swagger信息——apiInfo（如果不配，会有默认）
    private ApiInfo apiInfo() {
        Contact DEFAULT_CONTACT = new Contact("SniperCoding", "https://github.com/SniperCoding", "********@qq.com");
        return new ApiInfo(
                "VUniversity",
                "VUniversity项目的API管理",
                "1.0",
                "",
                DEFAULT_CONTACT,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
