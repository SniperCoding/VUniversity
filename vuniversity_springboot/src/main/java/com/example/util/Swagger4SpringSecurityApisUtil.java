package com.example.util;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Swagger4SpringSecurityApisUtil implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        // 登录接口
        // 1.定义参数
        Parameter username = new ParameterBuilder()
                .name("username")
                .description("用户名")
                .type(new TypeResolver().resolve(String.class))
                .modelRef(new ModelRef("string"))
                .parameterType("form")
                .required(true)
                .defaultValue("张三")
                .build();
        Parameter password = new ParameterBuilder()
                .name("password")
                .description("密码")
                .type(new TypeResolver().resolve(String.class))
                .modelRef(new ModelRef("string"))
                .parameterType("form")
                .required(true)
                .defaultValue("123")
                .build();

        Parameter code = new ParameterBuilder()
                .name("code")
                .description("验证码")
                .type(new TypeResolver().resolve(String.class))
                .modelRef(new ModelRef("string"))
                .parameterType("form")
                .required(true)
                .defaultValue("123")
                .build();

        Parameter verifyKey = new ParameterBuilder()
                .name("verifyKey")
                .description("验证key")
                .type(new TypeResolver().resolve(String.class))
                .modelRef(new ModelRef("string"))
                .parameterType("form")
                .required(true)
                .defaultValue("123")
                .build();

        // 2.接口的每种请求方式(GET/POST...)为一个 Operation
        Operation loginOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("登录")
                .tags(Sets.newHashSet("Authentication"))
                .responseMessages(Sets.newHashSet(new ResponseMessageBuilder().code(200).message("OK").build()))
                .consumes(Sets.newHashSet(MediaType.MULTIPART_FORM_DATA_VALUE))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .parameters(Arrays.asList(username, password,code,verifyKey))
                .build();
        //3.每个接口路径对应一个 ApiDescription
        ApiDescription loginDesc = new ApiDescription(null, "/login", "登录", Arrays.asList(loginOperation), false);
        documentationContext.getTags().add(new Tag("Authentication", "登录"));
        List<ApiDescription> apiDescriptionList = new ArrayList<>(Arrays.asList(loginDesc));
        return apiDescriptionList;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
