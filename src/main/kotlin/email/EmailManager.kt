package online.marcel.email

import io.ktor.client.plugins.api.Send
import online.marcel.db.DBManager
import online.marcel.email.dataclass.EmailNotificationRow
import online.marcel.email.dataclass.SendMailData
import java.sql.Connection

class EmailManager {

    private val emailPersistence = EmailPersistence()

    fun insertSetNotificationDate(list: List<EmailNotificationRow>) {
        DBManager.getConnection().use { conn: Connection ->
            for(row: EmailNotificationRow in list) {
                emailPersistence.insertSendNotificationDate(conn, row.id)
            }
        }
    }

    fun getAllNotificatonData(): Result<List<SendMailData>> {
        return try {
            val listfromdb: List<EmailNotificationRow> = this.emailPersistence.getEmailNotificationListWithoutAlreadySend()
            val groupedByEmail: MutableMap<String, MutableList<EmailNotificationRow>> = mutableMapOf<String, MutableList<EmailNotificationRow>>()
            val result: MutableList<SendMailData> = mutableListOf<SendMailData>()

            for (row: EmailNotificationRow in listfromdb) {
                groupedByEmail.getOrPut(row.ownerEmail) { mutableListOf() }.add(row)
            }

            for ((email: String, row: MutableList<EmailNotificationRow>) in groupedByEmail) {
                result.add(SendMailData(email = email, rowlist = row))
            }

            Result.success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}