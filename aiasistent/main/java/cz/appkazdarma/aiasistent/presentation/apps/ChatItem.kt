package cz.appkazdarma.aiasistent.presentation.apps

import cz.appkazdarma.aiasistent.common.Resource
import cz.appkazdarma.aiasistent.domain.model.GeminiItem

sealed class ChatItem {
    data class UserRequest(val request: String) : ChatItem()
    data class GeminiResponse(val response: Resource<GeminiItem>) : ChatItem()
}