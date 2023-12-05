package com.kuro9.libraend.ws.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kuro9.libraend.db.type.DeskTable

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SeatListInputForm(
    val key: String,
    val seats: List<SeatState>,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SeatListOutputForm(
    val seatStateChange: List<SeatState>,
    val seatTimeout: List<SeatError>,
    val desk: List<DeskTable>,
)
