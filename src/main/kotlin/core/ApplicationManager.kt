package online.marcel.core

import io.ktor.server.plugins.NotFoundException
import online.marcel.db.DBManager
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

object ApplicationManager {

    fun getApplicationUser(email: String): Result<ApplicationUser> {
        val stm = "SELECT id, email FROM login WHERE email = ? AND deleted IS NULL"

        DBManager.getConnection().use { conn: Connection ->
            conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
                pStmt.setString(1, email)
                pStmt.executeQuery().use { rs: ResultSet ->
                    if (rs.next()) {
                        return Result.success(ApplicationUser(rs.getInt(1), rs.getString(2)))
                    }
                }
            }
        }

        return Result.failure(NotFoundException("Applicationuser not found"))
    }

}