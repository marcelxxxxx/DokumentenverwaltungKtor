package online.marcel.tools

import java.security.MessageDigest

object Hasher {

    /**
     * generiert aus dem übergebenen String einen Hash und liefert diesen an den Aufrufer zurück
     * @param clearstring der String der gehasht werden soll
     * @return der gehashte String
     * @author Marcel Blankschein
     * @since v1.0
     */
    fun generatePasswordHash(clearstring: String, salt: String): String {
        return if (clearstring != "") {
            this.hashString(salt + clearstring + salt)
        } else {
            clearstring
        }
    }

    //unterstützt SHA-256 und MD5
    private fun hashString(input: String, algorithm: String = "SHA-256"): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

}