package com.euzhene.euzhpad_debug.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.euzhene.euzhpad_debug.data.database.NoteItemDbModel
import com.euzhene.euzhpad_debug.data.database.NoteListDao
import com.euzhene.euzhpad_debug.data.mapper.NoteListMapper
import com.euzhene.euzhpad_debug.domain.entity.Filter
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import com.euzhene.euzhpad_debug.domain.repository.NoteListRepository
import java.lang.RuntimeException
import javax.inject.Inject

class NoteListRepositoryImpl @Inject constructor(
    private val noteListDao: NoteListDao,
    private val noteListMapper: NoteListMapper,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) : NoteListRepository {
    private val mediatorLiveData = MediatorLiveData<List<NoteItem>>()
    override fun getNoteList(filter: Filter): LiveData<List<NoteItem>> {
        val filterString = when (filter) {
            Filter.BY_TITLE -> FILTER_TITLE
            Filter.BY_CHANGE_DATE -> FILTER_CHANGE_DATE
            Filter.BY_CREATE_DATE -> FILTER_CREATE_DATE
            Filter.BY_DEFAULT -> sharedPreferencesRepository.filter
        }
        mediatorLiveData.apply {
            removeSource(getSource())
        }
        sharedPreferencesRepository.updateFilterValue(filterString)

        return mediatorLiveData.apply {
            addSource(getSource()) {
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

    override suspend fun getNoteItem(noteItemId: Int): NoteItem {
        val noteItemDbModel = noteListDao.getNoteItemById(noteItemId)
        return noteListMapper.mapDbModelToEntity(noteItemDbModel)
    }

    override fun getDefaultFilter(): Filter {
        return when (sharedPreferencesRepository.filter) {
            FILTER_TITLE -> Filter.BY_TITLE
            FILTER_CREATE_DATE -> Filter.BY_CREATE_DATE
            FILTER_CHANGE_DATE -> Filter.BY_CHANGE_DATE
            else -> throw RuntimeException(
                "Unknown filter type ${sharedPreferencesRepository.filter}"
            )
        }
    }

    private fun getSource(): LiveData<List<NoteItemDbModel>> {
        return when (sharedPreferencesRepository.filter) {
            FILTER_TITLE -> noteListDao.getNoteListByTitle()
            FILTER_CHANGE_DATE -> noteListDao.getNoteListByLastEditTime()
            FILTER_CREATE_DATE -> noteListDao.getNoteListByCreateDate()
            else -> throw RuntimeException(
                "Unknown filter type ${sharedPreferencesRepository.filter}"
            )
        }
    }

    companion object {
        private const val FILTER_TITLE = "title"
        private const val FILTER_CHANGE_DATE = "lastEditTime"
        private const val FILTER_CREATE_DATE = "createDate"
    }

}