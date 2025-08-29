package cz.appkazdarma.aiasistent.presentation.home

sealed class HomeUIEvent {
    data class ShowSnackbar(val message: String) : HomeUIEvent()
}