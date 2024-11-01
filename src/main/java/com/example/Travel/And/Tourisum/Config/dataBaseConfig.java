package com.example.Travel.And.Tourisum.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
// Jdbc Template is Used to write Sql Commands to interact with DataBase
// Querying DataBase
@Configuration
public class dataBaseConfig {
    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
