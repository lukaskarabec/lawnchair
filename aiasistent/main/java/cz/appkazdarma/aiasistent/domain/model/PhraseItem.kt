package cz.appkazdarma.aiasistent.domain.model

/**
 * A quick translation phrase.
 */
data class PhraseItem(
    val id: String,
    val original: String,
    val translation: String,
    val languagePair: String,
    val createdAt: Long
)
