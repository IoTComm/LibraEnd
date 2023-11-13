package com.kuro9.libraend.router

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.router.config.API_PATH
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.errorhandle.withError
import com.kuro9.libraend.router.type.ReservationReturnForm
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_PATH + "library/")
class LibraryRoute {

    @Autowired
    lateinit var db: DBHandler

    @PostMapping("reservation", "login")
    fun reservation(
        @CookieValue(COOKIE_SESS_KEY) sessId: String,
        @RequestBody body: ReservationReturnForm,
        response: HttpServletResponse
    ): BasicReturnForm {
        val (seatId, startTime) = body
        val result = kotlin.runCatching { db.libraryReservation(seatId, startTime, sessId) }
            .getOrElse { withError(it) }

        response.status = result.code
        return result
    }

    @PostMapping("logout")
    fun libraryLogout(@CookieValue(COOKIE_SESS_KEY) sessId: String, response: HttpServletResponse): BasicReturnForm {
        val result = kotlin.runCatching { db.libraryLogout(sessId) }
            .getOrElse { withError(it) }

        response.status = result.code
        return result
    }
}