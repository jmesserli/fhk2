package nu.peg.fhk2.util

/**
 * By joel @ 19.11.2016
 */

private val hexChars = "0123456789abcdef".toCharArray()

fun ByteArray.toHex(): String {
    val sb = StringBuilder()
    this.map { it.toInt() }.forEach { sb.append("${hexChars[it shr 4 and 0xf]}${hexChars[it and 0xf]}") }
    return sb.toString()
}