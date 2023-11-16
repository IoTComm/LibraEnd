package com.kuro9.libraend.router.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.sql.Timestamp

data class LoginInputForm(
    @Schema(description = "ID", example = "2100123", required = true)
    val id: Int,
    @Schema(description = "Password", example = "q1w2e3r4", required = true)
    val pw: String
)

@JsonNaming(SnakeCaseStrategy::class)
data class ReservationInputForm(
    @Schema(description = "seat id to reservation", example = "5", required = true)
    val seatId: Int
) {
    val startTime: Timestamp = Timestamp(System.currentTimeMillis() + 10000)
}
