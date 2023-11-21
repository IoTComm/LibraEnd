package com.kuro9.libraend.router

import com.kuro9.libraend.router.config.ROOT_PATH
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ROOT_PATH)
class RootRoute {
    @GetMapping
    fun index() = "./index.html"

    @GetMapping("library/login")
    fun libraryLogin() = "./"
}