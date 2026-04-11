package online.marcel.register

import java.sql.Connection
import java.sql.PreparedStatement

class RegisterPersistence {

    fun registerNewUser(conn: Connection, email: String, hashPassword: String, salt: String) {
        val stm = "INSERT INTO login (email, hashpasswort, salt) VALUES (?, ?, ?)"

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setString(1, email)
            pStmt.setString(2, hashPassword)
            pStmt.setString(3, salt)
            pStmt.execute()
        }
    }

}