package com.losolved.inventorymanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "config.datasource")
public class DataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

}
