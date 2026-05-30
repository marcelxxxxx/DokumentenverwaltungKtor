package online.marcel.file.dataclass

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class EmailNotificationRowFromDB(val id: Int, val fileid: Int, val date: LocalDate)
