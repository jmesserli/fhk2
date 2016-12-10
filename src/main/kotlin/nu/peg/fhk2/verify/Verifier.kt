package nu.peg.fhk2.verify

import nu.peg.fhk2.digest.Digest
import java.nio.file.Path

/**
 * joel @10.12.2016
 */
interface Verifier {
    fun getVerifyFileVersion(): Int
    fun createVerifyFile(fileToDigest: Path, digestEnum: Digest)
    fun verifyFile(verifyFile: Path, fileToVerify: Path): Boolean
}