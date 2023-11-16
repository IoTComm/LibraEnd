package com.kuro9.libraend.db.type

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema

data class BasicReturnForm<T>(
    @Schema(description = "응답코드", example = "200")
    val code: Int,
    @Schema(description = "설명", example = "OK")
    val description: String,
    @Schema(description = "추가 데이터")
    var data: T? = null
) {
    @JsonIgnore
    fun getBasicForm() = BasicReturnForm<Nothing>(code, description)
}