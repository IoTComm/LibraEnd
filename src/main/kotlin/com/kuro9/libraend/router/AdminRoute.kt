package com.kuro9.libraend.router

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.db.type.LastUsedReturnForm
import com.kuro9.libraend.router.config.API_PATH
import com.kuro9.libraend.router.config.COOKIE_SESS_KEY
import com.kuro9.libraend.router.errorhandle.withError
import com.kuro9.libraend.router.type.SudoLogoutInputForm
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_PATH + "admin/")
class AdminRoute {

    @Autowired
    lateinit var db: DBHandler

    @PostMapping("sudo-logout")
    fun sudoLogout(
        @CookieValue(COOKIE_SESS_KEY) sessId: String,
        @RequestBody body: SudoLogoutInputForm,
        response: HttpServletResponse
    ): BasicReturnForm<LastUsedReturnForm> =
        runCatching { db.sudoLibrarySeatClear(sessId, body.seatId) }
            .getOrElse { withError(it) }
            .also { response.status = it.code }
}