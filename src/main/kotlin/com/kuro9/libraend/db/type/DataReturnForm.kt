package com.kuro9.libraend.db.type

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class LastUsedReturnForm(val lastUsedUserId: Int)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SessIdReturnForm(val sessId: String)
