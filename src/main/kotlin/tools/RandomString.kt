package online.marcel.tools

object RandomString {

    fun generateRandomString(length: Int): String {
        val chars: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-"
        return (1..length).map { chars.random() }.joinToString("")
    }

}