package com.kuro9.libraend.ws.type

data class WSMessage<T>(
    val type: Int,
    val data: T,
)
