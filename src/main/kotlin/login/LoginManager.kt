package online.marcel.login

import java.sql.Connection

class LoginManager {

    private val loginPersistence = LoginPersistence()

    fun handleLogin(login: Login): Result<Boolean> {
        try {
            val email: String = login.email
            val password: String = login.password

            if (email.isEmpty() || !password.contains("@")) {
                return Result.failure(Exception("Bitte gib eine gültige Email-Adresse ein"))
            }

            if (password.isEmpty()) {
                return Result.failure(Exception("Das Passwort darf nicht leer sein"))
            }


        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
        return Result.success(true)
    }

    fun getUserByMail(conn: Connection, mail: String) : Result<UserFromLogin> {
        return runCatching {
            val user: UserFromLogin? = loginPersistence.getUserByMail(conn, mail)
            user ?: throw Exception("User zur Email $mail nicht gefunden")
        }
    }

}