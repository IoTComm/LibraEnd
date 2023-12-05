package com.kuro9.libraend.ws.fallback

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "libraend.admin")
data class AdminConfigure(
    val key: String,
    val timeLimit: Long,
)