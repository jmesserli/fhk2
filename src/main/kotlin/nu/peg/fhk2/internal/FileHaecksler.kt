package nu.peg.fhk2.internal

import nu.peg.fhk2.util.FileDigester
import nu.peg.fhk2.util.internal.SHA256FileDigester
import nu.peg.fhk2.util.toHex
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * By joel @ 18.11.2016
 */
class FileHaecksler {
    companion object {
        private val filePattern = Regex("^.*\\.(\\d+|verify)\\.fhk2$")
    }

    fun cutFile(file: Path, partSize: Long, generateValidateFile: Boolean) {
        val totalSize = Files.size(file)
        val parts = Math.ceil(totalSize.toDouble() / partSize).toLong()
        var transferLeft = totalSize

        val fromChannel = Files.newByteChannel(file, StandardOpenOption.READ) as FileChannel

        for (i in 0..parts - 1) {
            val targetFileName = "${file.toAbsolutePath()}.$i.fhk2"
            print("\rCutting into $targetFileName [${i + 1}/$parts]")
            val toChannel = Files.newByteChannel(Paths.get(targetFileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING) as FileChannel
            val toTransfer = if (transferLeft < partSize) {
                transferLeft
            } else {
                partSize
            }
            var transfered = 0L

            while (toTransfer - transfered > 0) {
                transfered += fromChannel.transferTo(0, toTransfer - transfered, toChannel)
            }
            toChannel.close()
            transferLeft -= transfered
        }
        print("\rCutting [Done]")
        fromChannel.close()

        if (generateValidateFile) {
            print("\nHashing the source file for later verification...")

            val digester: FileDigester = SHA256FileDigester()
            val digest = digester.digestFile(file)
            val digestBuffer = ByteBuffer.allocate(digest.size)
            digestBuffer.put(digest)
            digestBuffer.position(0)
            println("\rHashing the source file [Done] (${digest.toHex()})")

            val targetFileName = "${file.toAbsolutePath()}.verify.fhk2"
            val channel = Files.newByteChannel(Paths.get(targetFileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
            channel.write(digestBuffer)
            channel.close()
        }
    }

    fun mergeFile(partFile: Path, verify: Boolean) {

    }
}