package online.marcel.tools

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

object LoginTools {

    fun hashPassword(password: String): String {
        val argon2: Argon2 = Argon2Factory.create()
        return argon2.hash(10, 65536, 2, password.toCharArray())
    }

    fun validatePassword(storedHash: String, password: String): Boolean {
        val argon2: Argon2 = Argon2Factory.create()
        // Validate the password against the hash
        return argon2.verify(storedHash, password.toCharArray())
    }

}