package nu.peg.fhk2.util.internal

import nu.peg.fhk2.util.FileDigester
import org.bouncycastle.crypto.digests.GeneralDigest
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path

/**
 * By joel @ 19.11.2016
 */
open class GeneralFileDigester(val digest: GeneralDigest, val blockSize: Int) : FileDigester {
    var hasRun = false
        private set


    override fun digestFile(file: Path): ByteArray {
        if (hasRun) throw IllegalStateException("You can run an instance of $javaClass only once!")

        val channel = Files.newByteChannel(file)
        val size = channel.size()

        // TODO optimize
        val buffer = ByteBuffer.allocate(blockSize)
        while (size - channel.position() > 0) {
            channel.read(buffer)
            while (buffer.hasRemaining()) digest.update(buffer.get())
            buffer.clear()
        }

        val targetArray = ByteArray(digest.digestSize)
        digest.doFinal(targetArray, 0)

        hasRun = true
        return targetArray
    }
}