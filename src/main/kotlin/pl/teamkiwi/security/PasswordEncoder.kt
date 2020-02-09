package pl.teamkiwi.security

import at.favre.lib.crypto.bcrypt.BCrypt

class PasswordEncoder {

    fun encode(password: String): String =
        BCrypt.withDefaults()
            .hashToString(PASSWORD_HASH_COST, password.toCharArray())

    fun isValid(password: String, hash: String) =
        BCrypt.verifyer()
            .verify(password.toCharArray(), hash.toCharArray()).verified

    companion object {
        private const val PASSWORD_HASH_COST = 12
    }
}