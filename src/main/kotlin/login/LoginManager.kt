package online.marcel.login

import online.marcel.db.DBManager
import online.marcel.tools.Hasher
import java.security.MessageDigest
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

                val hashPassword = Hasher.generatePasswordHash(password, user.salt)
                if (MessageDigest.isEqual(hashPassword.toByteArray(), user.hashPassword.toByteArray())) {
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
        return Result.success(false)
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