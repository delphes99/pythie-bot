package fr.delphes.utils

import java.security.MessageDigest
import java.util.Base64

fun String.toSha256(): ByteArray {
    val digesterSecretString = MessageDigest.getInstance("SHA-256")
    digesterSecretString.update(this.toByteArray())
    return digesterSecretString.digest()
}



fun ByteArray.toBase64(): String {
    return Base64
        .getEncoder()
        .encodeToString(this)
}