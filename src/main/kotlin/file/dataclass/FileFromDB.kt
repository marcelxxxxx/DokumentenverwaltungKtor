package online.marcel.file.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class FileFromDB(val id: Int, val originalFilename: String, val newFilename: String, var filetype: String? = null)