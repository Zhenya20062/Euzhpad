package com.euzhene.euzhpad_debug.domain.usecase

import androidx.lifecycle.LiveData
import com.euzhene.euzhpad_debug.domain.entity.Filter
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import com.euzhene.euzhpad_debug.domain.repository.NoteListRepository
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(private val repository: NoteListRepository) {
    operator fun invoke(filter: Filter): LiveData<List<NoteItem>> {
        return repository.getNoteList(filter)
    }
}