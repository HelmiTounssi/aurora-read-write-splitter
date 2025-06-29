package com.aurora.config;

import com.aurora.routing.ReadWriteRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {
    
    // Configuration temporairement désactivée pour permettre le démarrage
    // avec la configuration DataSource standard de Spring Boot
    
    /*
    @Bean
    @ConfigurationProperties("spring.datasource.writer")
    public DataSourceProperties writerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.writer.hikari")
    public DataSource writerDataSource(@Qualifier("writerDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.reader")
    public DataSourceProperties readerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.reader.hikari")
    public DataSource readerDataSource(@Qualifier("readerDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public DataSource routingDataSource(@Qualifier("writerDataSource") DataSource writerDataSource,
                                         @Qualifier("readerDataSource") DataSource readerDataSource) {
        ReadWriteRoutingDataSource routingDataSource = new ReadWriteRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("WRITER", writerDataSource);
        targetDataSources.put("READER", readerDataSource);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(writerDataSource);
        return routingDataSource;
    }
    */
} 