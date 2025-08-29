package cz.appkazdarma.aiasistent.domain.model

data class DigitalHygieneConfig(
    val id: String,
    val quietIntervals: List<QuietInterval>,
    val exceptions: List<String>
)

data class QuietInterval(val start: String, val end: String, val days: List<Int>)
