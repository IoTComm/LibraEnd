package com.kuro9.libraend.sse.type

/* level=0: 중요하지 않음, level=1: 사용자에게 즉시 알려야 됨(경고), level=2: 긴급*/
data class Notify(
    val level: Int,
    val message: String,
)
