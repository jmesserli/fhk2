package nu.peg.fhk2.util

/**
 * By joel @ 19.11.2016
 */
object FileSizeParser {
    val suffixMultipliers: Map<Char, Double> = mapOf(
            'k' to factor(1) / 8,
            'K' to factor(1),
            'm' to factor(2) / 8,
            'M' to factor(2),
            'g' to factor(3) / 8,
            'G' to factor(3)
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