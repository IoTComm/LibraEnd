package com.kuro9.libraend.db

import com.kuro9.libraend.db.type.BasicReturnForm
import com.kuro9.libraend.db.type.UuidReturnForm
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.sql.CallableStatement
import java.sql.SQLException
import java.sql.SQLTimeoutException
import java.sql.Timestamp
import java.sql.Types
import java.sql.Types.INTEGER
import java.sql.Types.VARCHAR
import kotlin.jvm.Throws

@Service
class DBHandler {
    @Autowired
    lateinit var dataSource: HikariDataSource

    private final val SIGNUP_QUERY = "CALL register_user(?, ?, ?)";
    private final val LOGIN_QUERY = "CALL user_login(?, ?, ?, ?)";
    private final val LIBRARY_RESERVATION_QUERY = "CALL library_reservation(?, ?, ?, ?)";
    private final val LIBRARY_LOGOUT_QUERY = "CALL library_logout(?, ?)";
    private final val LIBRARY_SEAT_LIST_QUERY = "CALL library_seat_list(?, ?, ?)";

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun registerUser(id: Int, pw: String): BasicReturnForm {
        if(!checkPWString(pw)) return BasicReturnForm(400, "Not Valid PW Form!")
        return SIGNUP_QUERY.query {
            with(it) {
                setInt(1, id)
                setString(2, pw)
                registerOutParameter(3, INTEGER)

                executeUpdate()

                val code = getInt(3)

                BasicReturnForm(
                    code, when (code) {
                        200 -> "OK"
                        409 -> "User already Exists!"
                        500 -> "DB Error"
                        else -> throw IllegalStateException("registerUser")
                    }
                )
            }
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun userLogin(id: Int, pw: String): UuidReturnForm {
        if(!checkPWString(pw))
            return UuidReturnForm(
                BasicReturnForm(
                    400, "Not Valid PW Form!"
                ),
                null
            )

        return LOGIN_QUERY.query {
            with(it) {
                setInt(1, id)
                setString(2, pw)
                registerOutParameter(3, INTEGER)
                registerOutParameter(4, java.sql.Types.VARCHAR)

                executeUpdate()

                val code = getInt(3)
                val sessKey = getString(4)

                UuidReturnForm(
                    BasicReturnForm(code, when(getInt(3)) {
                        200 -> "OK"
                        401 -> "ID/PW not correct!"
                        500 -> "DB Error"
                        else -> throw IllegalStateException("userLogin")
                    }),
                    when(code) {
                        200 -> sessKey
                        else -> null
                    }
                )
            }
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun libraryReservation(seatId: Int, startTime: Timestamp, sessId: String): BasicReturnForm
        = LIBRARY_RESERVATION_QUERY.query {
        with(it) {
            setInt(1, seatId)
            setTimestamp(2, startTime)
            setString(3, sessId)
            registerOutParameter(4, INTEGER)

            executeUpdate()

            val code = getInt(4)

            BasicReturnForm(code, when(code) {
                200 -> "OK"
                400 -> "Not valid time!"
                401 -> "Not valid cert!"
                403 -> "You already have reservation!"
                404 -> "Seat Not Found!"
                409 -> "Already have reservation on selected time!"
                500 -> "DB Error"
                else -> throw IllegalStateException("libraryReserv.")
            })
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun libraryLogout(sessId: String): BasicReturnForm = LIBRARY_LOGOUT_QUERY.query {
        with(it) {
            setString(1, sessId)
            registerOutParameter(2, INTEGER)

            executeUpdate()

            val code = getInt(2)

            BasicReturnForm(code, when(code) {
                200 -> "OK"
                500 -> "DB Error"
                else -> throw IllegalStateException("libraryLogout")
            })
        }
    }

    private fun checkPWString(pw: String): Boolean
        = Regex("^[a-zA-Z0-9!@#\$%^&*()_+-=]{1,15}$").matches(pw)


    private fun <T> String.query(action: (CallableStatement) -> T): T {
        dataSource.connection.use { connection ->
            connection.prepareCall(this).use {
                return action(it)
            }
        }
    }
}

