package nu.peg.fhk2.util

import java.nio.channels.FileChannel

object FileChannelUtil {

    fun transferAmount(from: FileChannel, to: FileChannel, offset: Long, amountBytes: Long) {
        var toTransfer = amountBytes

        while (toTransfer > 0)
            toTransfer -= from.transferTo(offset, toTransfer, to)
    }

    fun transferAllFromBeginning(from: FileChannel, to: FileChannel) {
        transferAmount(from, to, 0, from.size())
    }
}