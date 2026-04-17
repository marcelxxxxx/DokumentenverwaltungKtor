package online.marcel.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(val email: String, val password: String, val confirmPassword: String)
