package com.iflove.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote swagger接口文档配置
 */
@Configuration
@Slf4j
public class SwaggerConfiguration {
    // TODO 编写接口文档
    @Bean
    public OpenAPI swaggerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("IM")
                        // 信息
                        .contact(new Contact().name("苍镜月").email("2307984361@qq.com").url("地址"))
                        // 简介
                        .description("IM即时通信系统")
                        // 版本
                        .version("v1")
                        // 许可证
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))

                .externalDocs(new ExternalDocumentation()
                        .description("外部文档")
                        .url("https://springshop.wiki.github.org/docs"));
    }
}
