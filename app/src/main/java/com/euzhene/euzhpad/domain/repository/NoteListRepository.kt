package com.euzhene.euzhpad.domain.repository

import androidx.lifecycle.LiveData
import com.euzhene.euzhpad.domain.entity.Filter
import com.euzhene.euzhpad.domain.entity.NoteItem

interface NoteListRepository {
    fun getNoteList(filter: Filter): LiveData<List<NoteItem>>
    suspend fun deleteNoteItem(noteItem: NoteItem)
    suspend fun addNoteItem(noteItem: NoteItem)
    suspend fun editNoteItem(noteItem: NoteItem)
    suspend fun getNoteItem(noteItemId: Int): NoteItem

    fun getDefaultFilter():Filter
}