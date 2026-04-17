package online.marcel.login

import online.marcel.db.DBManager
import online.marcel.tools.LoginTools
import java.sql.Connection

class LoginManager {

    private val loginPersistence = LoginPersistence()

    fun handleLogin(loginRequest: LoginRequest): Result<Boolean> {
        try {
            val email: String = loginRequest.email
            val password: String = loginRequest.password

            if (email.isEmpty() || !email.contains("@")) {
                return Result.failure(Exception("Bitte gib eine gültige Email-Adresse ein"))
            }

            if (password.isEmpty()) {
                return Result.failure(Exception("Das Passwort darf nicht leer sein"))
            }

            val resultUser: Result<UserFromLogin> = this.getUserByMail(email)

            return if (resultUser.isFailure) {
                Result.failure(Exception("Logindaten ungültig"))
            } else {
                val user: UserFromLogin = resultUser.getOrThrow()
                if (LoginTools.validatePassword(user.hashPassword, password)) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Logindaten ungültig"))
                }

                //TODO noch mit einbauen, dass wegen TOTP gecheckt wird
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    fun getUserByMail(conn: Connection, mail: String) : Result<UserFromLogin> {
        return runCatching {
            val user: UserFromLogin? = loginPersistence.getUserByMail(conn, mail)
            user ?: throw Exception("User zur Email $mail nicht gefunden")
        }
    }

    fun getUserByMail(mail: String) : Result<UserFromLogin> {
        return runCatching {
            DBManager.getConnection().use { conn: Connection ->
                val user: UserFromLogin? = loginPersistence.getUserByMail(conn, mail)
                user ?: throw Exception("User zur Email $mail nicht gefunden")
            }
        }
    }

}