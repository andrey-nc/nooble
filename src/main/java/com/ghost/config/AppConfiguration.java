package com.ghost.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${lucene.data.directory}")
    private String dataDirectory;

    @Value("${lucene.index.directory}")
    private String indexDirectory;

    public String getAppName() {
        return appName;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }
}
