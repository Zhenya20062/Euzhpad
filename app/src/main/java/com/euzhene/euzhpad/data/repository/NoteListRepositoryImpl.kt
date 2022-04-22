package com.euzhene.euzhpad.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.euzhene.euzhpad.data.database.NoteListDao
import com.euzhene.euzhpad.data.mapper.NoteListMapper
import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.domain.repository.NoteListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteListRepositoryImpl @Inject constructor(
    private val noteListDao: NoteListDao,
    private val noteListMapper: NoteListMapper
) : NoteListRepository {
    override fun getNoteList(): LiveData<List<NoteItem>> {

        return MediatorLiveData<List<NoteItem>>().apply {
            addSource(noteListDao.getNoteList()) {
                value = noteListMapper.mapDbModelListToEntity(it)
            }
        }
    }

    override suspend fun deleteNoteItem(noteItem: NoteItem) {
        noteListDao.deleteNoteItem(noteItem.id)
    }

    override suspend fun addNoteItem(noteItem: NoteItem) {
        val noteItemDbModel = noteListMapper.mapEntityToDbModel(noteItem)
        noteListDao.addNoteItem(noteItemDbModel)
    }

    override suspend fun editNoteItem(noteItem: NoteItem) {
        val noteItemDbModel = noteListMapper.mapEntityToDbModel(noteItem)
        noteListDao.addNoteItem(noteItemDbModel)
    }

}