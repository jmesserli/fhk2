package nu.peg.fhk2.internal

/**
 * By joel @ 19.11.2016
 */
object FileSizeParser {

    val suffixMultipliers: Map<Char, Double> = mapOf(
            Pair('k', factor(1) / 8),
            Pair('K', factor(1)),
            Pair('m', factor(2) / 8),
            Pair('M', factor(2)),
            Pair('g', factor(3) / 8),
            Pair('G', factor(3))
    )

    private fun factor(level: Int): Double {
        return Math.pow(1024.0, level.toDouble())
    }

    val validationRegex = Regex("^(\\d+)([${suffixMultipliers.keys.joinToString()}])?$")

    /**
     * Parses the passed size string
     * @param sizeString A size string, supports suffixes like M or G
     * @return The parsed size in bytes
     * @throws IllegalArgumentException If the string contains an unknown suffix
     */
    fun parseFileSize(sizeString: String): Long {
        val result = validationRegex.matchEntire(sizeString)
        if (result is MatchResult) {
            val group = result.groups[2] ?: return sizeString.toLong()
            return (suffixMultipliers[group.value.toCharArray()[0]]!! * result.groups[1]!!.value.toLong()).toLong()
        }
        throw IllegalArgumentException("Invalid size string given. Must match <$validationRegex>")
    }
}