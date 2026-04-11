package online.marcel.register

import kotlinx.serialization.Serializable

@Serializable
data class Register(val email: String, val password: String, val confirmPassword: String, val datenschutz: String)
