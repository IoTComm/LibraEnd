package com.kuro9.libraend.db

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class DataSourceConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    fun dataSourceProperties() = DataSourceProperties()

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    fun dataSource(properties: DataSourceProperties): HikariDataSource =
        properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
}