package com.kuro9.libraend.sse.type

data class SseMessage<T>(
    val type: Int,
    val data: T,
)