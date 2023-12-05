package com.kuro9.libraend.db

import com.kuro9.libraend.db.type.*
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.CallableStatement
import java.sql.SQLException
import java.sql.SQLTimeoutException
import java.sql.Timestamp
import java.sql.Types.BOOLEAN
import java.sql.Types.INTEGER

@Service
class DBHandler {
    @Autowired
    lateinit var dataSource: HikariDataSource

    private final val SIGNUP_QUERY = "CALL register_user(?, ?, ?)"
    private final val LOGIN_QUERY = "CALL user_login(?, ?, ?, ?)"
    private final val LIBRARY_RESERVATION_QUERY = "CALL library_reservation(?, ?, ?, ?)"
    private final val LIBRARY_LOGOUT_QUERY = "CALL library_logout(?, ?)"
    private final val LIBRARY_SEAT_LIST_QUERY = "CALL library_seat_list(?, ?, ?)"
    private final val IS_ADMIN_QUERY = "CALL is_admin(?, ?)"
    private final val SUDO_LIBRARY_CLEAR_SEAT_QUERY = "CALL sudo_library_clear_seat(?, ?, ?, ?)"
    private final val GET_USER_ID = "{ ? = CALL get_user_id(?) }"
    private final val GET_SEAT_ID = "{ ? = CALL get_user_seat(?) }"

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun registerUser(id: Int, pw: String): BasicReturnForm<Nothing> {
        if (!checkPWString(pw)) return BasicReturnForm(400, "Not Valid PW Form!")
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
    fun userLogin(id: Int, pw: String): BasicReturnForm<SessIdReturnForm> {
        if (!checkPWString(pw))
            return BasicReturnForm(400, "Not Valid PW Form!", null)

        return LOGIN_QUERY.query {
            with(it) {
                setInt(1, id)
                setString(2, pw)
                registerOutParameter(3, INTEGER)
                registerOutParameter(4, java.sql.Types.VARCHAR)

                executeUpdate()

                val code = getInt(3)
                val sessKey = getString(4)

                BasicReturnForm(
                    code, when (getInt(3)) {
                        200 -> "OK"
                        401 -> "ID/PW not correct!"
                        500 -> "DB Error"
                        else -> throw IllegalStateException("userLogin")
                    },
                    when (code) {
                        200 -> SessIdReturnForm(sessKey)
                        else -> null
                    }
                )
            }
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun libraryReservation(seatId: Int, startTime: Timestamp, sessId: String?): BasicReturnForm<Nothing> =
        LIBRARY_RESERVATION_QUERY.query {
            with(it) {
                setInt(1, seatId)
                setTimestamp(2, startTime)
                setString(3, sessId)
                registerOutParameter(4, INTEGER)

                executeUpdate()

                val code = getInt(4)

                BasicReturnForm(
                    code, when (code) {
                        200 -> "OK"
                        400 -> "Not valid time!"
                        401 -> "Not valid cert!"
                        403 -> "You already have reservation!"
                        404 -> "Seat Not Found!"
                        409 -> "Already have reservation on selected time!"
                        500 -> "DB Error"
                        else -> throw IllegalStateException("libraryReserv.")
                    }
                )
            }
        }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun libraryLogout(sessId: String?): BasicReturnForm<Nothing> = LIBRARY_LOGOUT_QUERY.query {
        with(it) {
            setString(1, sessId)
            registerOutParameter(2, INTEGER)

            executeUpdate()

            val code = getInt(2)

            BasicReturnForm(
                code, when (code) {
                    200 -> "OK"
                    500 -> "DB Error"
                    else -> throw IllegalStateException("libraryLogout")
                }
            )
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun getSeatList(
        seatId: Int? = null,
        isUsing: Boolean? = null,
        deskId: Int? = null,
    ): BasicReturnForm<List<SeatTable>> {
        val resultList = mutableListOf<SeatTable>()

        LIBRARY_SEAT_LIST_QUERY.query {
            with(it) {
                if (seatId != null) setInt(1, seatId)
                else setNull(1, INTEGER)
                if (isUsing != null) setBoolean(2, isUsing)
                else setNull(2, BOOLEAN)
                if (deskId != null) setInt(3, deskId)
                else setNull(3, INTEGER)

                executeQuery().use { resultSet ->
                    with(resultSet) {
                        while (next())
                            resultList.add(
                                SeatTable(
                                    seatId = getInt(1),
                                    isUsing = getBoolean(2),
                                    deskId = getInt(3)
                                )
                            )
                    }
                }
            }
        }

        return BasicReturnForm(200, "${resultList.size}", resultList)
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun isAdmin(sessId: String): Int = IS_ADMIN_QUERY.query {
        with(it) {
            setString(1, sessId)
            registerOutParameter(2, INTEGER)

            executeQuery()

            getInt(2)
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun sudoLibrarySeatClear(sessId: String?, seatId: Int): BasicReturnForm<LastUsedReturnForm> {
        return SUDO_LIBRARY_CLEAR_SEAT_QUERY.query {
            with(it) {
                setString(1, sessId)
                setInt(2, seatId)
                registerOutParameter(3, INTEGER)
                registerOutParameter(4, INTEGER)

                executeUpdate()

                val code = getInt(3)
                val lastUserId = getInt(4)

                BasicReturnForm(
                    code, when (code) {
                        200 -> "OK"
                        401 -> "Not valid cert!"
                        403 -> "Access Denied"
                        404 -> "Seat Not Found!"
                        500 -> "Internal Server Error"
                        else -> "Internal Server Error!"
                    },
                    when (code) {
                        200 -> LastUsedReturnForm(lastUserId)
                        else -> null
                    }
                )
            }
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun getUserId(sessId: String?): BasicReturnForm<UserIdReturnForm> {
        if (sessId === null) return BasicReturnForm(401, "Not Logged In")

        return GET_USER_ID.query {
            with(it) {
                setString(2, sessId)
                registerOutParameter(1, INTEGER)

                execute()

                when (val userId = getInt(1)) {
                    0 -> BasicReturnForm(400, "Not valid sessId")
                    else -> BasicReturnForm(200, "OK", UserIdReturnForm(userId))
                }
            }
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun getSeatId(userId: Int): BasicReturnForm<SeatIdReturnForm> {
        return GET_SEAT_ID.query {
            with(it) {
                setInt(2, userId)
                registerOutParameter(1, INTEGER)

                execute()
                var seatId: Int? = getInt(1)
                if (seatId == 0) seatId = null

                BasicReturnForm(200, "OK", SeatIdReturnForm(seatId))
            }
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun getSeatId(sessId: String?): BasicReturnForm<SeatIdReturnForm> {
        val userIdResult = getUserId(sessId)
        if (userIdResult.code != 200) {
            return BasicReturnForm(userIdResult.code, userIdResult.description)
        }

        return GET_SEAT_ID.query {
            with(it) {
                setInt(2, userIdResult.data!!.userId)
                registerOutParameter(1, INTEGER)

                execute()

                var seatId: Int? = getInt(1)
                if (seatId == 0) seatId = null

                BasicReturnForm(200, "OK", SeatIdReturnForm(seatId))
            }
        }
    }

    @Throws(SQLException::class, SQLTimeoutException::class, IllegalStateException::class)
    fun updateDeskStatus(seatId: Int): DeskTable {
        val desk: DeskTable
        dataSource.connection.use {
            it.createStatement().use {
                val deskResult = it.executeQuery("SELECT desk_id FROM seat WHERE id = $seatId")
                if (!deskResult.next()) throw IllegalStateException("invalid seat id!")
                val deskId = deskResult.getInt(1)
                if (deskId == 0) throw IllegalStateException("seat has desk of null!")
                deskResult.close()

                val seatResult = it.executeQuery("SELECT * FROM seat WHERE desk_id = $deskId and is_using = 0")
                val isDeskUsing = seatResult.next()
                // 현재 책상에서 사용중인 좌석이 없다면 false else true
                it.executeUpdate("UPDATE desk SET state = $isDeskUsing WHERE id = $deskId")
                seatResult.close()

                desk = DeskTable(deskId, isDeskUsing)
            }
        }
        return desk
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun getDeskStatus(): List<DeskTable> {
        val deskStatus = mutableListOf<DeskTable>()
        dataSource.connection.use {
            it.createStatement().use {
                it.executeQuery("SELECT * FROM desk").use {
                    while (it.next()) deskStatus.add(
                        DeskTable(it.getInt(1), it.getBoolean(2))
                    )
                }
            }
        }
        return deskStatus
    }

    @Throws(SQLException::class, SQLTimeoutException::class)
    fun getSessId(seatId: Int): String? {
        var userId: Int? = null
        var sessId: String? = null
        dataSource.connection.use {
            it.createStatement().use {
                it.executeQuery("SELECT last_user_id FROM seat WHERE id = $seatId").use {
                    if (it.next()) userId = it.getInt(1)
                }
                if (userId == null) {
                    println("userid got null")
                    return null
                }

                it.executeQuery("SELECT session_key FROM user WHERE id = $userId").use {
                    if (it.next()) sessId = it.getString(1)
                }
            }
        }
        return sessId
    }

    private fun checkPWString(pw: String): Boolean = Regex("^[a-zA-Z0-9!@#\$%^&*()_+-=]{1,15}$").matches(pw)


    private fun <T> String.query(action: (CallableStatement) -> T): T {
        dataSource.connection.use { connection ->
            connection.prepareCall(this).use {
                return action(it)
            }
        }
    }
}

