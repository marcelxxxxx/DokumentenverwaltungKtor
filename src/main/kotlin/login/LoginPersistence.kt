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
        val stm = "SELECT id, email, hashpasswort FROM login WHERE email = ? AND deleted IS NULL";
        var user: UserFromLogin? = null

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setString(1, mail)
            pStmt.executeQuery().use { rs: ResultSet ->
                if (rs.next()) {
                    val id: Int = rs.getInt(1)
                    val email = rs.getString(3)
                    val hashPassword = rs.getString(3)
                    user = UserFromLogin(id, email, hashPassword)
                }
            }
        }
        return user
    }

    fun updateLastLogin(user: UserFromLogin) : Result<Boolean> {
        try {
            val stm = "UPDATE login SET lastLogin = ? WHERE id = ? AND deleted IS NULL";
            DBManager.getConnection().use { conn: Connection ->
                conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
                    pStmt.setObject(1, DateTimeHelper.getCurrentDateTime().toJavaLocalDateTime())
                    pStmt.setInt(2, user.id)
                    pStmt.executeUpdate()
                    return Result.success(true)
                }
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}