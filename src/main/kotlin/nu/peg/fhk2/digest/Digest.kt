package nu.peg.fhk2.digest

import nu.peg.fhk2.digest.internal.SHA128FileDigester
import nu.peg.fhk2.digest.internal.SHA256FileDigester
import nu.peg.fhk2.digest.internal.SHA512FileDigester
import kotlin.reflect.KClass

/**
 * This enum collects all available [FileDigester]s and implicitly assigns them ordinals that will be used in verify files
 */
enum class Digest(val digesterClass: KClass<out FileDigester>) {
    // *** ALWAYS ADD NEW DIGESTERS AT THE BOTTOM, YOU'LL SCREW UP THE ORDINALS USED IN THE VERIFY FILE OTHERWISE ***
    SHA256(SHA256FileDigester::class),
    SHA128(SHA128FileDigester::class),
    SHA512(SHA512FileDigester::class),
    ;

    companion object {
        fun byNameOrOrdinal(nameOrOrdinal: String): Digest? {
            try {
                val ordinal = nameOrOrdinal.toInt()
                return Digest.values().filter { it.ordinal == ordinal }.firstOrNull()
            } catch(nfe: NumberFormatException) {
                val lowerCaseName = nameOrOrdinal.toLowerCase()
                return values().filter { it.name.toLowerCase() == lowerCaseName }.firstOrNull()
            }
        }
    }
}