package com.seminav.newsapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/news/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
        registry.addMapping("/admin/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PATCH", "PUT");
    }
}
