package nu.peg.fhk2.digest

import nu.peg.fhk2.digest.internal.SHA256FileDigester
import kotlin.reflect.KClass

/**
 * This enum collects all available [FileDigester]s and implicitly assigns them ordinals that will be used in verify files
 */
enum class Digest(val digesterClass: KClass<out FileDigester>) {
    // *** ALWAYS ADD NEW DIGESTERS AT THE BOTTOM, YOU'LL SCREW UP THE ORDINALS USED IN THE VERIFY FILE OTHERWISE ***
    SHA256(SHA256FileDigester::class)
}