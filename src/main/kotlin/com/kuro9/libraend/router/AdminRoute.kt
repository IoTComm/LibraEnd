package com.kuro9.libraend.router

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.db.type.LastUsedReturnForm
import com.kuro9.libraend.router.config.API_PATH
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.errorhandle.withError
import com.kuro9.libraend.router.type.SudoLogoutInputForm
import com.kuro9.libraend.sse.SseController
import com.kuro9.libraend.sse.type.Notify
import com.kuro9.libraend.ws.observer.SeatStateBroadcaster
import com.kuro9.libraend.ws.type.SeatState
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
@RequestMapping(API_PATH + "admin/")
@Tag(name = "Admin API", description = "열람실 관리 API입니다.")
class AdminRoute {

    @Autowired
    lateinit var db: DBHandler

    @Autowired
    lateinit var seatBroadcaster: SeatStateBroadcaster

    @Autowired
    lateinit var notify: SseController

    @PostMapping("sudo-logout")
    @Operation(description = "강제 로그아웃")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Request success",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Not valid certification",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Access Denied",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not valid parameter[seat_id] (seat not exist)",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            )
        ]
    )
    fun sudoLogout(
        @CookieValue(COOKIE_SESS_KEY) sessId: String?,
        @RequestBody body: SudoLogoutInputForm,
        response: HttpServletResponse,
    ): BasicReturnForm<LastUsedReturnForm> =
        runCatching {
            notify.notifyClientWithSeat(body.seatId, Notify(1, "관리자에 의해 강제 퇴실 처리 되었습니다. "))
            db.sudoLibrarySeatClear(sessId, body.seatId)
        }
            .getOrElse { withError(it) }
            .also {
                response.status = it.code
                seatBroadcaster.updateState(SeatState(body.seatId, false))
            }
}