package nu.peg.fhk2.verify

import nu.peg.fhk2.digest.Digest
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.*
import java.util.*
import kotlin.reflect.primaryConstructor

object Verifier {
    const val VERIFY_FILE_VERSION: Int = 1

    fun createVerifyFile(fileToDigest: Path, digestEnum: Digest) {
        val digester = digestEnum.digesterClass.primaryConstructor!!.call()
        val digest = digester.digestFile(fileToDigest)

        val digestBuffer = ByteBuffer.allocate(digest.size)
        digestBuffer.put(digest)
        digestBuffer.flip()

        val targetFileName = "${fileToDigest.toAbsolutePath()}.verify.fhk2"
        val channel = Files.newByteChannel(Paths.get(targetFileName), CREATE, WRITE, TRUNCATE_EXISTING)

        // Version specific part
        val versionDigestBuffer = ByteBuffer.allocate(8) // Two Ints are 64 bits wide, allocate takes bytes
        versionDigestBuffer.putInt(VERIFY_FILE_VERSION).putInt(digestEnum.ordinal)
        versionDigestBuffer.flip()
        channel.write(versionDigestBuffer)
        // End version specific part

        channel.write(digestBuffer)
        channel.close()
    }

    fun verifyFile(verifyFile: Path, fileToVerify: Path): Boolean {
        if (!Files.exists(verifyFile)) error("Verify file does not exist")

        val verifyChannel = Files.newByteChannel(verifyFile, READ)

        val fourBytes = ByteBuffer.allocate(4)
        verifyChannel.read(fourBytes)
        fourBytes.flip()
        val fileVersion = fourBytes.int
        if (fileVersion != VERIFY_FILE_VERSION)
            error("Verify file version is not supported by this version of the application (file version: $fileVersion, program version: $VERIFY_FILE_VERSION)")

        // Version specific part
        // reuse byte buffer
        fourBytes.clear()
        verifyChannel.read(fourBytes)
        fourBytes.flip()
        val digestOrdinal = fourBytes.int

        if (Digest.values().size < digestOrdinal - 1)
            error("Digest used in verify file is not supported (fileDigest: $digestOrdinal, supported: ${Digest.values().joinToString(", ") { it.ordinal.toString() }})")

        val digest = Digest.values()[digestOrdinal]
        val digester = digest.digesterClass.primaryConstructor!!.call()
        // End version specific part

        val digestBytes = digester.digestFile(fileToVerify)
        val digestBuffer = ByteBuffer.allocate(digestBytes.size)
        verifyChannel.read(digestBuffer)
        verifyChannel.close()

        val shouldBeDigestBytes = ByteArray(digestBytes.size)
        digestBuffer.flip()
        digestBuffer.get(shouldBeDigestBytes)

        return Arrays.equals(shouldBeDigestBytes, digestBytes)
    }
}