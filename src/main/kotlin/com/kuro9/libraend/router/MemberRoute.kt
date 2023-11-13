package com.kuro9.libraend.router

import com.kuro9.libraend.db.DBHandler
import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.db.type.UuidReturnForm
import com.kuro9.libraend.router.config.API_PATH
import com.kuro9.libraend.router.config.getCookie
import com.kuro9.libraend.router.errorhandle.withError
import com.kuro9.libraend.router.type.LoginInputForm
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_PATH + "member/")
class MemberRoute {
    @Autowired
    lateinit var db: DBHandler

    @PostMapping("signup")
    fun signUp(@RequestBody body: LoginInputForm, response: HttpServletResponse): BasicReturnForm =
        runCatching { db.registerUser(body.id, body.pw) }
            .getOrElse { withError(it) }
            .also { response.status = it.code }


    @PostMapping("login")
    fun login(@RequestBody body: LoginInputForm, response: HttpServletResponse): BasicReturnForm =
        runCatching { db.userLogin(body.id, body.pw) }
            .getOrElse { UuidReturnForm(withError(it), null) }
            .also {
                response.status = it.result.code

                if (it.result.code == 200)
                    response.addCookie(getCookie(it.uuid!!))
            }.result


}