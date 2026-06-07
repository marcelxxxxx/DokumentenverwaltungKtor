package online.marcel.file.dataclass

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FileFromDB(val id: Int, val originalFilename: String, val newFilename: String, var filetype: String? = null, var date: LocalDate? = null, var sendnotification: LocalDateTime? = null)