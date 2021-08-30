package br.com.shopcars.extensions

import java.security.MessageDigest

fun md5(string: String) : ByteArray = MessageDigest.getInstance("MD5").digest(string.toByteArray(Charsets.UTF_8))
fun ByteArray.toHex() = joinToString(separator = "") {byte -> "%02x".format(byte)}