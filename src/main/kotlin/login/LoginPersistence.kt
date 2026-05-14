package online.marcel.login

import kotlinx.datetime.toJavaLocalDateTime
import online.marcel.db.DBManager
import online.marcel.tools.DateTimeHelper
import java.sql.Connection
import java.sql.Date
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

    fun updateLastLogin(user: UserFromLogin) : Result<Boolean> {
        try {
            val stm = "UPDATE login SET lastLogin = ? WHERE email = ? AND deleted IS NULL";
            DBManager.getConnection().use { conn: Connection ->
                conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
                    pStmt.setObject(1, DateTimeHelper.getCurrentDateTime().toJavaLocalDateTime())
                    pStmt.setString(2, user.email)
                    pStmt.executeUpdate()
                    return Result.success(true)
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}