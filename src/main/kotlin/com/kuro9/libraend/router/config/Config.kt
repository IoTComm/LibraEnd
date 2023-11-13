package com.kuro9.libraend.router.config

import jakarta.servlet.http.Cookie

const val ROOT_PATH = "/iotcomm/"
const val API_PATH = ROOT_PATH + "api/"
const val COOKIE_SESS_KEY = "sess-id"
fun getCookie(sessId: String): Cookie =
    Cookie(COOKIE_SESS_KEY, sessId).apply {
        domain = "localhost" //TODO 나중에 변경 필요
        path = "/"
        maxAge = 6 * 60 * 60
        // secure = true
        // isHttpOnly = true

    }