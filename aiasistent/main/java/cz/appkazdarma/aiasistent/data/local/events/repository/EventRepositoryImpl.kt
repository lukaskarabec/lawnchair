package cz.appkazdarma.aiasistent.data.local.events.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.os.Build
import cz.appkazdarma.aiasistent.data.local.events.EventDao
import cz.appkazdarma.aiasistent.data.local.events.entity.EventEntity
import cz.appkazdarma.aiasistent.domain.model.EventItem
import cz.appkazdarma.aiasistent.domain.repository.EventRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger

class EventRepositoryImpl @Inject constructor(
    private val dao: EventDao
) : EventRepository {

    private val firebaseRef get() =
        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "unknown")
            .child("events")

    private val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    private val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    override suspend fun addOrUpdateEvent(item: EventItem) {
        val entity = EventEntity.fromEventItem(item)
        dao.upsert(entity)
        val datePath = dateFormat.format(Date(entity.createdAt))
        val entry = mapOf(
            "title" to entity.title,
            "description" to entity.description,
            "date" to timestampFormat.format(Date(entity.date)),
            "time" to entity.time,
            "createdAt" to timestampFormat.format(Date(entity.createdAt)),
            "deviceModel" to Build.MODEL
        )
        firebaseRef.child(datePath).child(entity.id).setValue(entry)
        FirebaseLogger.log("events/add", entry)
    }

    override suspend fun getEvents(): List<EventItem> {
        return dao.getEvents().map { it.toEventItem() }
    }

    override suspend fun deleteEvent(id: String) {
        dao.deleteEvent(id)
        val datePath = dateFormat.format(Date())
        firebaseRef.child(datePath).child(id).removeValue()
        FirebaseLogger.log("events/delete", mapOf("id" to id))
    }
}
