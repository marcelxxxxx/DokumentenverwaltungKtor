package online.marcel.email.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class SendMailData(val email: String, val rowlist: List<EmailNotificationRow>)
