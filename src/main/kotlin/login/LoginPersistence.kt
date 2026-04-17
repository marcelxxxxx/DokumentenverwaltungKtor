package online.marcel.login

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class LoginPersistence {

    fun getUserByMail(conn: Connection, mail: String): UserFromLogin? {
        val stm = "SELECT email, hashpasswort FROM login WHERE email = ? AND deleted IS NULL";
        var user: UserFromLogin? = null

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setString(1, mail)
            pStmt.executeQuery().use { rs: ResultSet ->
                if (rs.next()) {
                    val email = rs.getString(1)
                    val hashPassword = rs.getString(2)
                    user = UserFromLogin(email, hashPassword)
                }
            }
        }
        return user
    }

}