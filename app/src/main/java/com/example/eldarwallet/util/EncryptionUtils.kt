package com.example.eldarwallet.util

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class EncryptionUtils {

    companion object {
        private const val FIXED_KEY = "1414141414141414" // 16 or 32 bytes long
    }

    fun encryptStringFixedKey(message: String, key: String): String {
        val messageBytes = message.toByteArray()
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

        val iv = ByteArray(16)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(iv)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(key.toByteArray(), "AES"),
            IvParameterSpec(iv)
        )
        val encryptedBytes = cipher.doFinal(messageBytes)

        return Base64.encodeToString(iv, Base64.DEFAULT) + "|" + Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decryptString(encryptedData: String, key: String): String {
        val encryptedBytes: ByteArray
        val iv: ByteArray
        val parts = encryptedData.split("|")

        if (parts.size == 2) {
            iv = Base64.decode(parts[0], Base64.DEFAULT)
            encryptedBytes = Base64.decode(parts[1], Base64.DEFAULT)
        } else {
            return ""
        }
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key.toByteArray(), "AES"), IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return String(decryptedBytes)
    }

    fun encryptStringFixedKey(message: String): String {
        val messageBytes = message.toByteArray()
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

        val iv = ByteArray(16)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(iv)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(Companion.FIXED_KEY.toByteArray(), "AES"),
            IvParameterSpec(iv)
        )
        val encryptedBytes = cipher.doFinal(messageBytes)

        return Base64.encodeToString(iv, Base64.DEFAULT) + "|" + Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decryptStringFixedKey(encryptedData: String): String {
        val encryptedBytes: ByteArray
        val iv: ByteArray
        val parts = encryptedData.split("|")

        if (parts.size == 2) {
            iv = Base64.decode(parts[0], Base64.DEFAULT)
            encryptedBytes = Base64.decode(parts[1], Base64.DEFAULT)
        } else {
            return ""
        }
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(Companion.FIXED_KEY.toByteArray(), "AES"), IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return String(decryptedBytes)
    }


}