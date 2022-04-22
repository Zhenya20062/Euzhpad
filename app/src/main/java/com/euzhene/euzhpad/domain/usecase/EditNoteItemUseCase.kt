package com.euzhene.euzhpad.domain.usecase

import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.domain.repository.NoteListRepository

class EditNoteItemUseCase(private val repository: NoteListRepository) {
    suspend operator fun invoke(noteItem: NoteItem) {
        repository.editNoteItem(noteItem)
    }
}