package com.euzhene.euzhpad.domain.usecase

import androidx.lifecycle.LiveData
import com.euzhene.euzhpad.domain.entity.Filter
import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.domain.repository.NoteListRepository
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(private val repository: NoteListRepository) {
    operator fun invoke(filter: Filter): LiveData<List<NoteItem>> {
        return repository.getNoteList(filter)
    }
}