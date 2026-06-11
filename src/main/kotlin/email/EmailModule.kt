package online.marcel.email

import io.ktor.server.application.*
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.*
import online.marcel.email.dataclass.EmailNotificationRow
import online.marcel.email.dataclass.SendMailData
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


fun Application.emailModule() {
    val emailManager = EmailManager()

    val job: Job = launchPeriodicTask(interval = 5.minutes, scope = this) {
        checkSendMail(emailManager)
    }

    monitor.subscribe(ApplicationStopped) {
        job.cancel()
    }
}

fun checkSendMail(emailmanager: EmailManager) {
    println("Checking for emails")
    val notificationlist: MutableList<EmailNotificationRow> = mutableListOf()
    val resultList: Result<List<SendMailData>> = emailmanager.getAllNotificatonData()

    if (resultList.isSuccess) {
        val list: List<SendMailData> = resultList.getOrThrow()

        for(sendmaildata: SendMailData in list) {
            val builder: StringBuilder = StringBuilder()

            for (notificationrow: EmailNotificationRow in sendmaildata.rowlist) {
                builder.appendLine(notificationrow.filename + ": " + notificationrow.date.toString())
                notificationlist.add(notificationrow)
            }

            sendMail(sendmaildata.email, builder.toString())
        }

        emailmanager.insertSetNotificationDate(notificationlist)
    } else {
        println("[checkSendMail] SendMailData failed")
    }
}

fun sendMail(to: String, messagetext: String) {

    val props: Properties = Properties().apply {
        put("mail.smtp.host", "smtp.strato.de")
        put("mail.smtp.port", "587")
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
    }

    val session: Session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(MailData.email, MailData.password)
            }
        }
    )

    try {
        val message: Message = MimeMessage(session)
        message.setFrom(InternetAddress(MailData.email))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
        message.subject = "Benachrichtigung über abzulaufende Dokumente"
        message.setContent(messagetext, "text/html; charset=utf-8")

        Transport.send(message)
    } catch (e: MessagingException) {
        e.printStackTrace()
    }

    println("Mail an $to versendet")
}

fun launchPeriodicTask(interval: Duration, scope: CoroutineScope, funktion: suspend () -> Unit): Job {
    return scope.launch(Dispatchers.IO) {
        while (scope.isActive) {
            try {
                funktion()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(interval)
        }
    }
}