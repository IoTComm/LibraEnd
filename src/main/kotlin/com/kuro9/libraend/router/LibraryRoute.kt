package com.kuro9.libraend.router

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.db.type.SeatTable
import com.kuro9.libraend.router.config.API_PATH
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.errorhandle.withError
import com.kuro9.libraend.router.type.ReservationInputForm
import com.kuro9.libraend.router.type.SeatListInputForm
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_PATH + "library/")
class LibraryRoute {

    @Autowired
    lateinit var db: DBHandler

    @PostMapping("login")
    fun reservation(
        @CookieValue(COOKIE_SESS_KEY) sessId: String,
        @RequestBody body: ReservationInputForm,
        response: HttpServletResponse
    ): BasicReturnForm<Nothing> = runCatching { db.libraryReservation(body.seatId, body.startTime, sessId) }
        .getOrElse { withError(it) }
        .also { response.status = it.code }


    @PostMapping("logout")
    fun libraryLogout(
        @CookieValue(COOKIE_SESS_KEY) sessId: String,
        response: HttpServletResponse
    ): BasicReturnForm<Nothing> =
        kotlin.runCatching { db.libraryLogout(sessId) }
            .getOrElse { withError(it) }
            .also { response.status = it.code }


    @PostMapping("seat-list")
    fun getSeatList(
        @CookieValue(COOKIE_SESS_KEY) sessId: String,
        @RequestBody body: SeatListInputForm?,
        response: HttpServletResponse
    ): BasicReturnForm<List<SeatTable>> =
        runCatching { db.getSeatList(body?.seatId, body?.isUsing, body?.deskId) }
            .getOrElse { withError(it, listOf()) }
            .also { response.status = it.code }

}