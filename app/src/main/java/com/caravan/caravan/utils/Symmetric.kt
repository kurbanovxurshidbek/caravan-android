package com.caravan.caravan.utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Symmetric {

    private const val secretKey = "caravan"
    private var secretKeySpec: SecretKeySpec? = null
    private lateinit var key: ByteArray

    // Set key
    private fun setKey(myKey: String) {
        var sha: MessageDigest? = null

        try {
            key = myKey.toByteArray(charset("UTF-8"))
            sha = MessageDigest.getInstance("SHA-1")
            key = sha.digest(key)
            key = key.copyOf(16)
            secretKeySpec = SecretKeySpec(key, "AES")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    // Method to encrypt the secret text using key
    fun encrypt(strToEncrypt: String): String? {
        try {
            setKey(secretKey)
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            return Base64.getEncoder().encodeToString(cipher.doFinal
                (strToEncrypt.toByteArray(charset("UTF-8"))))
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error while encrypting: $e")
        }
        return null
    }

    // Method to decrypt the secret text using key
    fun decrypt(strToDecrypt: String?): String? {
        try {
            setKey(secretKey)
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            return String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)))
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error while decrypting: $e")
        }
        return null
    }

}