package online.marcel.email.dataclass

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class EmailNotificationRow(val id: Int, val fileid: Int, val filename: String, val date: LocalDate)
