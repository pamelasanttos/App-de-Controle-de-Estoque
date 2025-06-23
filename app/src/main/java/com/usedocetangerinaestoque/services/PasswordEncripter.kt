package com.usedocetangerinaestoque.services

import java.security.MessageDigest

class PasswordEncripter(private val secretKey: String) {

    fun encrypt(password: String): String {
        val newPassword = password + secretKey
        val bytes = newPassword.toByteArray(Charsets.UTF_8)

        val hashBytes = MessageDigest.getInstance("SHA-512").digest(bytes)

        return hashBytes.toHexString()
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }
}
