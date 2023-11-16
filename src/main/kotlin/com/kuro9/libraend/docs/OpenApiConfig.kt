package com.kuro9.libraend.docs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val info: Info = Info()
            .title("IoTComm Proj. API Document")
            .version("v0.0.1")
            .description("API Docs Here.")


//        // Security 스키마 설정
//        val bearerAuth: SecurityScheme = SecurityScheme()
//            .type(SecurityScheme.Type.HTTP)
//            .scheme("bearer")
//            .bearerFormat("JWT")
//            .`in`(SecurityScheme.In.HEADER)
//            .name(HttpHeaders.AUTHORIZATION)
//
//
//        // Security 요청 설정
//        val addSecurityItem = SecurityRequirement()
//        addSecurityItem.addList("JWT")
        return OpenAPI()
            .components(Components())
            .info(info)
            
    }
}