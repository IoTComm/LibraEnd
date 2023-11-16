package com.kuro9.libraend.router.errorhandle

import com.kuro9.libraend.db.type.BasicReturnForm
import java.sql.SQLException
import java.sql.SQLTimeoutException

fun <T> withError(e: Throwable, fallbackData: T? = null): BasicReturnForm<T> {
    e.printStackTrace()
    val result = when (e) {
        is SQLTimeoutException -> BasicReturnForm(504, "Internal Server Timeout!", fallbackData)
        is SQLException -> BasicReturnForm(500, "Server Error!", fallbackData)
        else -> BasicReturnForm(500, "Unknown Server Error!", fallbackData)
    }

    return result
}