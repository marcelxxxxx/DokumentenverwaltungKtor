package online.marcel.email

import kotlinx.datetime.toJavaLocalDateTime
import online.marcel.db.DBManager
import online.marcel.email.dataclass.EmailNotificationRow
import online.marcel.tools.DateTimeHelper
import online.marcel.tools.SQLHelper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class EmailPersistence {

    fun insertSendNotificationDate(conn: Connection, rowid: Int) {

        val stm = "UPDATE emailnotification SET sendnotification = ? WHERE id = ?;"

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setObject(1, DateTimeHelper.getCurrentDateTime().toJavaLocalDateTime())
            pStmt.setInt(2, rowid)
            pStmt.executeUpdate()
        }

    }

    fun getEmailNotificationListWithoutAlreadySend(conn: Connection): List<EmailNotificationRow> {
        val liste: MutableList<EmailNotificationRow> = mutableListOf()

        val stm: String = "SELECT en.id, en.fileid, file.originalFilename, en.date, login.email " +
                "FROM emailnotification AS en " +
                "JOIN file ON file.id = en.fileid AND file.deleted IS NULL " +
                "JOIN login ON (login.id = file.uploadFileBy) " +
                "WHERE en.sendnotification IS NULL AND en.deleted IS NULL AND en.date IS NOT NULL AND en.date <= CURDATE();"

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.executeQuery().use { rs: ResultSet ->
                while (rs.next()) {
                    val emailrow: EmailNotificationRow = EmailNotificationRow(
                        id = rs.getInt(1),
                        fileid = rs.getInt(2),
                        filename = rs.getString(3),
                        date = SQLHelper.getLocalDate(rs, 4)!!,
                        ownerEmail = rs.getString(5),
                    )
                    liste.add(emailrow)
                }
            }
        }
        return liste
    }

    fun getEmailNotificationListWithoutAlreadySend(): List<EmailNotificationRow> {
        DBManager.getConnection().use { conn: Connection ->
            return this.getEmailNotificationListWithoutAlreadySend(conn)
        }
    }

}