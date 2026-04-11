package online.marcel.login

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class LoginPersistence {

    fun getUserByMail(conn: Connection, mail: String): UserFromLogin? {
        val stm = "SELECT email, hashPassword, salt FROM login WHERE email = ?";
        var user: UserFromLogin? = null

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setString(1, mail)
            pStmt.executeQuery().use { rs: ResultSet ->
                if (rs.next()) {
                    val email = rs.getString(1)
                    val hashPassword = rs.getString(2)
                    val salt = rs.getString(3)
                    user = UserFromLogin(email, hashPassword, salt)
                }
            }
        }

        return user
    }

}