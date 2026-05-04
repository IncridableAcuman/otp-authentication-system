package com.sms.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}
