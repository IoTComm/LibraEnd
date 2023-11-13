package com.kuro9.libraend.router

import com.kuro9.libraend.API_PATH
import com.kuro9.libraend.ROOT_PATH
import jakarta.servlet.http.Cookie
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ROOT_PATH)
class RootRoute {
    @GetMapping
    fun index(): Pair<String, String> {
        return Pair("Hello", "World")
    }
}