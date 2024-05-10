package org.seminav.apigateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
//        registry.addMapping("/saved**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
//        registry.addMapping("/auth/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
//        registry.addMapping("/admin/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
//        registry.addMapping("/market/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
//        registry.addMapping("/news/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
//        registry.addMapping("/booking/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
    }
}
