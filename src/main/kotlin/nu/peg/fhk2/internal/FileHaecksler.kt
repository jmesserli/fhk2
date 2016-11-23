package nu.peg.fhk2.internal

import nu.peg.fhk2.digest.Digest
import nu.peg.fhk2.util.FileChannelUtil
import nu.peg.fhk2.verify.Verifier
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.*

/**
 * By joel @ 18.11.2016
 */
class FileHaecksler {
    companion object {
        private val filePattern = Regex("^(.*)\\.(\\d+|verify)\\.fhk2$")
    }

    fun cutFile(file: Path, partSize: Long, generateValidateFile: Boolean, deleteSources: Boolean) {
        val totalSize = Files.size(file)
        val parts = Math.ceil(totalSize.toDouble() / partSize).toLong()
        var transferLeft = totalSize

        val fromChannel = Files.newByteChannel(file, READ) as FileChannel

        for (i in 0..parts - 1) {
            val targetFileName = "${file.toAbsolutePath()}.$i.fhk2"
            print("\rCutting into $targetFileName [${i + 1}/$parts]")
            val toChannel = Files.newByteChannel(Paths.get(targetFileName), CREATE, WRITE, TRUNCATE_EXISTING) as FileChannel
            val toTransfer = if (transferLeft < partSize) transferLeft else partSize
            FileChannelUtil.transferAmount(fromChannel, toChannel, totalSize - transferLeft, toTransfer)
            toChannel.close()
            transferLeft -= toTransfer
        }
        println(" [Done]")
        fromChannel.close()

        if (generateValidateFile) {
            print("Hashing the source file for later verification")
            Verifier.createVerifyFile(file, Digest.SHA256)
            println(" [Done]")
        }

        if (deleteSources) {
            Files.delete(file)
            println("\nDeleting source file [Done]")
        }
    }

    fun mergeFile(partFile: Path, verify: Boolean, deleteSources: Boolean) {
        val matchResult = filePattern.matchEntire(partFile.fileName.toString()) ?: error("The passed source file does not match $filePattern")

        val pathList = mutableListOf<Path>()
        val originalFilename = matchResult.groups[1]!!.value

        // TODO maybe merge even if files don't start with 0
        for (i in 0..Int.MAX_VALUE) {
            val file = getFile(originalFilename, i.toString()) ?: break
            pathList.add(file)
        }
        if (pathList.isEmpty()) error("No files to merge have been found (maybe they don't start with index 0?)")

        println("Found ${pathList.size} files to merge")
        val verifyFile = getFile(originalFilename, "verify")
        if (verify && verifyFile == null)
            println("WARNING: Verification will not be available because no .verify.fhk2 file could be found.")

        val targetFile = Paths.get(partFile.toAbsolutePath().parent.toString(), originalFilename)
        val targetChannel = Files.newByteChannel(targetFile, CREATE, WRITE, TRUNCATE_EXISTING) as FileChannel

        pathList.forEachIndexed { index, originFile ->
            print("\rAppending $originFile [${index + 1}/${pathList.size}]")
            val originChannel = Files.newByteChannel(originFile, READ) as FileChannel
            FileChannelUtil.transferAllFromBeginning(originChannel, targetChannel)
            originChannel.close()
        }
        println(" [Done]")

        var valid = verifyFile != null
        if (verify && verifyFile != null) {
            print("Verifying file")
            valid = Verifier.verifyFile(verifyFile, targetFile)
            println(" [Done]")
            if (!valid) println("VERIFY ERROR: The file does not match the hash")
        }

        if (deleteSources) {
            if (valid) {
                pathList.forEach { Files.delete(it) }
            } else {
                println("INFO: Skipping source file deletion because file verification failed")
            }
        }
    }

    private fun getFile(filename: String, index: String): Path? {
        val path = Paths.get("$filename.$index.fhk2")

        return when (Files.exists(path)) {
            true -> path
            false -> null
        }
    }
}