package cz.appkazdarma.aiasistent.presentation.events

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.appkazdarma.aiasistent.domain.model.EventItem
import cz.appkazdarma.aiasistent.domain.use_case.add_event.AddEventUseCase
import cz.appkazdarma.aiasistent.domain.use_case.delete_event.DeleteEventUseCase
import cz.appkazdarma.aiasistent.domain.use_case.get_events.GetEventsUseCase
import cz.appkazdarma.aiasistent.domain.use_case.update_event.UpdateEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getEvents: GetEventsUseCase,
    private val addEvent: AddEventUseCase,
    private val updateEvent: UpdateEventUseCase,
    private val deleteEvent: DeleteEventUseCase
) : ViewModel() {

    private val _events = mutableStateOf<List<EventItem>>(emptyList())
    val events: State<List<EventItem>> = _events

    init {
        refreshEvents()
    }

    fun refreshEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = getEvents()
            withContext(Dispatchers.Main) { _events.value = items }
        }
    }

    fun addNewEvent(title: String, description: String, date: Long, time: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val event = EventItem(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                date = date,
                time = time,
                createdAt = now
            )
            addEvent(event)
            refreshEvents()
        }
    }

    fun updateEvent(item: EventItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateEvent(item)
            refreshEvents()
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteEvent(id)
            refreshEvents()
        }
    }
}
