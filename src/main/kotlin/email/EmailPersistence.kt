package online.marcel.email

import kotlinx.datetime.toJavaLocalDateTime
import online.marcel.email.dataclass.EmailNotificationRow
import online.marcel.tools.DateTimeHelper
import java.sql.Connection
import java.sql.PreparedStatement

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

        val stm: String = "SELECT en.id, en.fileid, file.originalFilename, en.date FROM emailnotification AS en " +
                "JOIN file ON (file.id = en.fileid AND file.deleted IS NULL) " +
                "AND en.sendnotification IS NULL AND en.deleted IS NULL;"


        return liste
    }

}