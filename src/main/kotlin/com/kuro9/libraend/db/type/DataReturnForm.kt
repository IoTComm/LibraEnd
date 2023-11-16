package com.kuro9.libraend.db.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class LastUsedReturnForm(
    @Schema(
        description = "last user that used this seat",
        example = "2100123"
    ) val lastUsedUserId: Int
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SessIdReturnForm(val sessId: String)
