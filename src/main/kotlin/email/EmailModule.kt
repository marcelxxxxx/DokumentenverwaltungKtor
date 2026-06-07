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
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


fun Application.emailModule() {
    val emailManager = EmailManager()

    val sendMailTo: List<String> = listOf("m_blankschein@web.de")

    val job: Job = launchPeriodicTask(interval = 1.minutes, scope = this) {
        sendMail(sendMailTo)
    }

    monitor.subscribe(ApplicationStopped) {
        job.cancel()
    }
}

fun sendMail(to: List<String>) {

    val props: Properties = Properties().apply {
        put("mail.smtp.host", "smtp.strato.de")
        put("mail.smtp.port", "587")
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
    }

    val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(MailData.email, MailData.password)
            }
        }
    )

    try {
        val message: Message = MimeMessage(session)
        message.setFrom(InternetAddress(MailData.email))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.joinToString(", ")))
        message.subject = "subject"
        message.setContent("messagetext", "text/html; charset=utf-8")

        Transport.send(message)
    } catch (e: MessagingException) {
        e.printStackTrace()
    }

    println("Mails an ${to.joinToString(", ")} versendet")
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