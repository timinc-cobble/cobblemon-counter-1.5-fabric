package us.timinc.mc.cobblemon.counter.util

import java.text.Normalizer
import java.util.regex.Pattern

object Util {
    fun cleanSpeciesNameForTranslation(input: String): String {
        // Normalize the string to NFD (Normalization Form D)
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)

        // Use a regular expression to remove all diacritical marks
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        val stripped = pattern.matcher(normalized).replaceAll("")

        return stripped
    }
}