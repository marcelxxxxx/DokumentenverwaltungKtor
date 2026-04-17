package online.marcel.login

import kotlinx.serialization.Serializable

@Serializable
data class UserFromLogin(val email: String, val hashPassword: String)