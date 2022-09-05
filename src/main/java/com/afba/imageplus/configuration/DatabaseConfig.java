package com.afba.imageplus.configuration;

import com.afba.imageplus.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean(name = ApplicationConstants.PRIMARY_DATASOURCE_BEAN)
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = ApplicationConstants.PRIMARY_JDBC_TEMPLATE_BEAN)
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier(ApplicationConstants.PRIMARY_DATASOURCE_BEAN) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = ApplicationConstants.SECONDARY_DATASOURCE_BEAN)
    @ConfigurationProperties(prefix = "spring.secondary-datasource")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = ApplicationConstants.SECONDARY_JDBC_TEMPLATE_BEAN)
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier(ApplicationConstants.SECONDARY_DATASOURCE_BEAN) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
