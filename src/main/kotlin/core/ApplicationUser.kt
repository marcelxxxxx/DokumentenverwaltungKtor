package online.marcel.core

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationUser(val id: Int, val email: String) {}