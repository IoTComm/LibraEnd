package com.kuro9.libraend

import com.kuro9.libraend.ws.fallback.AdminConfigure
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AdminConfigure::class)
class LibraEndApplication

fun main(args: Array<String>) {
    runApplication<LibraEndApplication>(*args)
}
