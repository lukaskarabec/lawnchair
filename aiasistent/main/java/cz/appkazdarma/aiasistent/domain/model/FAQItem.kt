package cz.appkazdarma.aiasistent.domain.model

/**
 * FAQ entry usable offline.
 */
data class FAQItem(
    val id: String,
    val question: String,
    val answer: String,
    val createdAt: Long,
    val custom: Boolean
)
