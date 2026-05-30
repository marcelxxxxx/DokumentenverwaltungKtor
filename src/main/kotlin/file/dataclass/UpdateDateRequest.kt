package online.marcel.file.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDateRequest(val documentid: Int, val email: String, val date: String)