package com.kuro9.libraend

import jakarta.servlet.http.Cookie
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

const val ROOT_PATH = "/iotcomm/"
const val API_PATH = ROOT_PATH + "api/"
const val COOKIE_SESS_KEY = "sess-id"

@SpringBootApplication
class LibraEndApplication

fun getCookie(sessId: String): Cookie =
	Cookie(COOKIE_SESS_KEY, sessId).apply {
		domain = "localhost" //TODO 나중에 변경 필요
		path = "/"
		maxAge =  6*60*60
		// secure = true
		// isHttpOnly = true

	}

fun main(args: Array<String>) {
	runApplication<LibraEndApplication>(*args)
}
