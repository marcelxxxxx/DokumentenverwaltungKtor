package online.marcel.login

import kotlinx.serialization.Serializable

@Serializable
data class UserFromLogin(val id: Int, val email: String, val hashPassword: String)