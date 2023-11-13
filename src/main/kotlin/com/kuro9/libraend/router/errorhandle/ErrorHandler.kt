package com.kuro9.libraend.router.errorhandle

import com.kuro9.libraend.db.type.BasicReturnForm
import java.sql.SQLException
import java.sql.SQLTimeoutException

fun withError(e: Throwable): BasicReturnForm {
    e.printStackTrace()
    val result = when (e) {
        is SQLTimeoutException -> BasicReturnForm(504, "Internal Server Timeout!")
        is SQLException -> BasicReturnForm(500, "Server Error!")
        else -> BasicReturnForm(500, "Unknown Server Error!")
    }

    return result
}