package cz.appkazdarma.aiasistent.ai

/**
 * Represents a response returned from an AI invocation.
 *
 * @property content The textual content produced by the model.
 * @property mode     Source of inference (on device or cloud).
 * @property modality Type of task that was performed.
 */
data class AiResponse(
    val content: String,
    val mode: AiMode,
    val modality: AiModality
)
