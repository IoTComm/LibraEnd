package com.kuro9.libraend.router

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.db.type.SeatIdReturnForm
import com.kuro9.libraend.db.type.UserIdReturnForm
import com.kuro9.libraend.router.config.API_PATH
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.config.getCookie
import com.kuro9.libraend.router.errorhandle.withError
import com.kuro9.libraend.router.type.LoginInputForm
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
@RequestMapping(API_PATH + "member/")
@Tag(name = "Member API", description = "Member 관련 API입니다.")
class MemberRoute {
    @Autowired
    lateinit var db: DBHandler

    @PostMapping("signup")
    @Operation(description = "회원가입")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Signup success",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "User already Exists",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
        ]
    )
    fun signUp(@RequestBody body: LoginInputForm, response: HttpServletResponse): BasicReturnForm<Nothing> =
        runCatching { db.registerUser(body.id, body.pw) }
            .getOrElse { withError(it) }
            .also { response.status = it.code }


    @PostMapping("login")
    @Operation(description = "로그인")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Login success",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "ID/PW not correct",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
        ]
    )
    fun login(@RequestBody body: LoginInputForm, response: HttpServletResponse): BasicReturnForm<Nothing> =
        runCatching { db.userLogin(body.id, body.pw) }
            .getOrElse { withError(it) }
            .also {
                response.status = it.code

                if (it.code == 200)
                    response.addCookie(getCookie(it.data!!.sessId))
                it.data = null
            }.getBasicForm()

    @GetMapping("my-id")
    @Operation(description = "내 아이디 가져오기")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Not logged in",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Not valid sessId",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
        ]
    )
    fun getMyId(
        @CookieValue(COOKIE_SESS_KEY) sessId: String?,
        response: HttpServletResponse
    ): BasicReturnForm<UserIdReturnForm> = runCatching { db.getUserId(sessId) }
        .getOrElse { withError(it) }
        .also { response.status = it.code }

    @GetMapping("my-seat")
    @Operation(description = "내 좌석 가져오기")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Not logged in",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Not valid sessId",
                content = [Content(schema = Schema(implementation = BasicReturnForm::class))]
            )
        ]
    )
    fun getMySeat(
        @CookieValue(COOKIE_SESS_KEY) sessId: String?,
        response: HttpServletResponse
    ): BasicReturnForm<SeatIdReturnForm> = runCatching { db.getSeatId(sessId) }
        .getOrElse { withError(it) }
        .also { response.status = it.code }

}