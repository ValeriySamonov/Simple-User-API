package com.example.user_api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserApiConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
