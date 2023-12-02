package com.kuro9.libraend.router

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.db.type.SeatTable
import com.kuro9.libraend.router.config.API_PATH
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.errorhandle.withError
import com.kuro9.libraend.router.type.ReservationInputForm
import com.kuro9.libraend.router.type.SeatListInputForm
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_PATH + "library/")
@Tag(name = "Library API", description = "열람실 관련 API입니다.")
class LibraryRoute {

    @Autowired
    lateinit var db: DBHandler

    @PostMapping("login")
    @Operation(description = "자리에 앉고 사용시작 요청")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Request success",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Not valid parameter[time] (maybe not return this)",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Not valid certification",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Already using other seat",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not valid parameter[seat_id] (seat not exist)",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Seat not available now (already using)",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            )
        ]
    )
    fun libraryLogin(
        @CookieValue(COOKIE_SESS_KEY) sessId: String?,
        @RequestBody body: ReservationInputForm,
        response: HttpServletResponse
    ): BasicReturnForm<Nothing> = runCatching { db.libraryReservation(body.seatId, body.startTime, sessId) }
        .getOrElse { withError(it) }
        .also { response.status = it.code }


    @PostMapping("logout")
    @Operation(description = "사용종료 요청")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Request success",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            )
        ]
    )
    fun libraryLogout(
        @CookieValue(COOKIE_SESS_KEY) sessId: String?,
        response: HttpServletResponse
    ): BasicReturnForm<Nothing> =
        kotlin.runCatching { db.libraryLogout(sessId) }
            .getOrElse { withError(it) }
            .also { response.status = it.code }


    @PostMapping("seat-list")
    @Operation(description = "현재 좌석 정보 검색")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Request success",
                useReturnTypeSchema = true
            )
        ]
    )
    fun getSeatList(
        @RequestBody body: SeatListInputForm?,
        response: HttpServletResponse
    ): BasicReturnForm<List<SeatTable>> =
        runCatching { db.getSeatList(body?.seatId, body?.isUsing, body?.deskId) }
            .getOrElse { withError(it, listOf()) }
            .also { response.status = it.code }

    @GetMapping("seat-list")
    @Operation(description = "현재 좌석 정보 출력")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Request success",
                useReturnTypeSchema = true
            )
        ]
    )
    fun getSeatList(
        response: HttpServletResponse
    ): BasicReturnForm<List<SeatTable>> = getSeatList(null, response)

}