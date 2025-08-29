package cz.appkazdarma.aiasistent.domain.repository

import cz.appkazdarma.aiasistent.domain.model.NoteItem

interface NoteRepository {
    suspend fun addOrUpdateNote(item: NoteItem)
    suspend fun getNotes(): List<NoteItem>
    suspend fun deleteNote(id: String)
}
