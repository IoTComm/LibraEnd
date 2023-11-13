package com.kuro9.libraend.router.type

import java.sql.Timestamp

data class LoginInputForm (
    val id: Int,
    val pw: String
)

data class ReserveInputForm (
    val seatId: Int,
    val startTime: Timestamp,
    val sessionId: String
)

data class LogoutInputForm (
    val sessionId: String
)