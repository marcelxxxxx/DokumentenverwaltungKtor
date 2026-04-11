package online.marcel.register

import online.marcel.db.DBManager
import online.marcel.login.LoginManager
import online.marcel.login.UserFromLogin
import online.marcel.tools.Hasher
import online.marcel.tools.RandomString
import java.sql.Connection

class RegisterManager {

    private val registerPersistence = RegisterPersistence()
    private val loginManager = LoginManager()

    fun registerNewUser(register: Register): Result<Boolean> {
        try {
            DBManager.getConnection().use { conn: Connection ->
                if (!this.isUserAlreadyRegistered(conn, register.email)) {

                    if (register.password != register.confirmPassword) {
                        return Result.failure(Exception("Passwörter stimmen nicht überein"))
                    } else {
                        val clearPassword: String = register.password
                        val salt: String = RandomString.generateRandomString(32)
                        val hashPassword: String = Hasher.generatePasswordHash(clearPassword, salt)

                        this.registerPersistence.registerNewUser(conn, register.email, hashPassword, salt)
                    }

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