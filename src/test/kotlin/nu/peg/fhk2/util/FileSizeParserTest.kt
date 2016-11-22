package nu.peg.fhk2.util

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * By joel @ 22.11.2016
 */
internal class FileSizeParserTest {
    val expectedFileSizes = mapOf(
            "10K" to 10240L, // 10 KiB
            "10k" to 1280L, // 10 kb
            "15M" to 15728640L, // 15 MiB
            "20m" to 2621440L, // 20 Mb
            "3G" to 3221225472L, // 3 GiB
            "2g" to 268435456L // 2 Gb
    )

    @Test
    fun parseFileSize() {
        expectedFileSizes.entries.forEach {
            assertThat(FileSizeParser.parseFileSize(it.key)).isEqualTo(it.value)
        }
    }
}