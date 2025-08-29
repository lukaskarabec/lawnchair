package cz.appkazdarma.aiasistent.data.local.notes.repository

import android.os.Build
import cz.appkazdarma.aiasistent.data.local.notes.NoteDao
import cz.appkazdarma.aiasistent.data.local.notes.entity.NoteEntity
import cz.appkazdarma.aiasistent.domain.model.NoteItem
import cz.appkazdarma.aiasistent.domain.repository.NoteRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import cz.appkazdarma.aiasistent.data.remote.firebase.FirebaseLogger

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    private val firebaseRef get() =
        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid ?: "unknown")
            .child("notes")

    private val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    private val timestampFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    override suspend fun addOrUpdateNote(item: NoteItem) {
        val entity = NoteEntity.fromNoteItem(item)
        dao.upsert(entity)
        val datePath = dateFormat.format(Date(entity.updatedAt))
        val entry = mapOf(
            "title" to entity.title,
            "content" to entity.content,
            "color" to entity.color,
            "labels" to entity.labels,
            "createdAt" to timestampFormat.format(Date(entity.createdAt)),
            "updatedAt" to timestampFormat.format(Date(entity.updatedAt)),
            "pinned" to entity.pinned,
            "deviceModel" to Build.MODEL
        )
        firebaseRef.child(datePath).child(entity.id).setValue(entry)
        FirebaseLogger.log("notes/add", entry)
    }

    override suspend fun getNotes(): List<NoteItem> {
        return dao.getNotes().map { it.toNoteItem() }
    }

    override suspend fun deleteNote(id: String) {
        dao.deleteNote(id)
        val datePath = dateFormat.format(Date())
        firebaseRef.child(datePath).child(id).removeValue()
        FirebaseLogger.log("notes/delete", mapOf("id" to id))
    }
}
