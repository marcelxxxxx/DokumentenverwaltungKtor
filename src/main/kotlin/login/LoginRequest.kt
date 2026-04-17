package online.marcel.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String, var hashPassword: String? = null, var salt: String? = null)
