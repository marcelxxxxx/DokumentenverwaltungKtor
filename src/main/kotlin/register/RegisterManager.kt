package online.marcel.register

import online.marcel.db.DBManager
import online.marcel.login.LoginManager
import online.marcel.login.UserFromLogin
import online.marcel.tools.Hasher
import online.marcel.tools.LoginTools
import online.marcel.tools.RandomString
import java.sql.Connection

class RegisterManager {

    private val registerPersistence = RegisterPersistence()
    private val loginManager = LoginManager()

    fun registerNewUser(registerRequest: RegisterRequest): Result<Boolean> {
        try {
            DBManager.getConnection().use { conn: Connection ->
                if (!this.isUserAlreadyRegistered(conn, registerRequest.email)) {

                    if (registerRequest.password != registerRequest.confirmPassword) {
                        return Result.failure(Exception("Passwörter stimmen nicht überein"))
                    } else {
                        val clearPassword: String = registerRequest.password
                        val hashPassword: String = LoginTools.hashPassword(clearPassword)
                        this.registerPersistence.registerNewUser(conn, registerRequest.email, hashPassword)
                    }
                } else {
                    return Result.failure(Exception("User bereits registriert"))
                }
            }
            return Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    private fun isUserAlreadyRegistered(conn: Connection, email: String): Boolean {
        val result: Result<UserFromLogin> = loginManager.getUserByMail(conn, email)
        return result.isSuccess

    }

}