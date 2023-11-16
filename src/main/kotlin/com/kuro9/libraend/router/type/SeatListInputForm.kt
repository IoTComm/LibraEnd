package com.kuro9.libraend.router.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SeatListInputForm(
    @Schema(description = "seat id to find", example = "5", required = false)
    val seatId: Int? = null,
    @Schema(description = "find using/!using seat", example = "true", required = false)
    val isUsing: Boolean? = null,
    @Schema(description = "find seat with desk id", example = "2", required = false)
    val deskId: Int? = null
)
