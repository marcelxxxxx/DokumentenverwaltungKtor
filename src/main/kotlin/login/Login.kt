package online.marcel.login

import kotlinx.serialization.Serializable

@Serializable
data class Login(val email: String, val password: String, var hashPassword: String? = null, var salt: String? = null)
