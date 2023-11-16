package com.kuro9.libraend.db.type

import com.fasterxml.jackson.annotation.JsonIgnore

data class BasicReturnForm<T>(
    val code: Int,
    val description: String,
    var data: T? = null
) {
    @JsonIgnore
    fun getBasicForm() = BasicReturnForm<Nothing>(code, description)
}