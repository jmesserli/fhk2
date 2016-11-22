package nu.peg.fhk2.digest

import java.nio.file.Path

/**
 * By joel @ 19.11.2016
 */
interface FileDigester {
    fun digestFile(file: Path): ByteArray
}