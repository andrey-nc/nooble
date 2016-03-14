package com.ghost.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    public String getAppName() {
        return appName;
    }
}
