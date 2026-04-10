package online.marcel.login

import online.marcel.tools.Result

class LoginManager {

    fun handleLogin(login: Login): Result<Boolean> {
        val result = Result<Boolean>()
        try {
            val email: String = login.email
            val password: String = login.password

            if (email.isEmpty() || !password.contains("@")) {
                result.addError("Email-Adresse ist nicht ungültig")
                return result
            }

            if (password.isEmpty()) {
                result.addError("Das Passwort darf nicht leer sein")
                return result
            }


        } catch (e: Exception) {
            e.printStackTrace()
            result.addError(e.message!!)
        }
        return result
    }

}