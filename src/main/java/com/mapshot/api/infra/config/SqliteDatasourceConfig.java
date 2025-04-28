package com.mapshot.api.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class SqliteDatasourceConfig {

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.driverClassName}")
    private String DRIVER_CLASS_NAME;


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(URL);

        return dataSource;
    }

}
