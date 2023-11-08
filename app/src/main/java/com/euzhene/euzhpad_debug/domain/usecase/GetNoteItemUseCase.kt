package com.euzhene.euzhpad_debug.domain.usecase

import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import com.euzhene.euzhpad_debug.domain.repository.NoteListRepository
import javax.inject.Inject

class GetNoteItemUseCase @Inject constructor (private val repository: NoteListRepository) {
    suspend operator fun invoke(noteItemId:Int):NoteItem {
        return repository.getNoteItem(noteItemId)
    }
}