package online.marcel.file

import kotlinx.serialization.Serializable

@Serializable
data class UploadFile(val originalFilename: String, val newFilename: String, val pathToFile: String, val uploadFrom: Int)
