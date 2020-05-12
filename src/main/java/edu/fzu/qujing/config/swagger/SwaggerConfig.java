package edu.fzu.qujing.config.swagger;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public Docket docket(Environment environment) {
        Profiles of = Profiles.of("dev", "test");
        boolean flag = environment.acceptsProfiles(of);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class)
                .groupName("ozg")
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.fzu.qujing.bean.controller"))
                .paths(PathSelectors.any())
                .build();
    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("取经接口")
                //创建人
                .contact(new Contact("mall-screen", "https://XXXX.com.cn", ""))
                //版本号
                .version("2.0")
                //描述
                .description("API 描述")
                .build();
    }
}
