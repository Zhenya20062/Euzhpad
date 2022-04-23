package com.euzhene.euzhpad.presentation.note_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.euzhene.euzhpad.domain.usecase.AddNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteItemUseCase
import java.lang.RuntimeException
import javax.inject.Inject

class NoteItemViewModelFactory @Inject constructor(
    private val addNoteItemUseCase: AddNoteItemUseCase,
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val getNoteItemUseCase: GetNoteItemUseCase,
    private val noteDate: NoteDate
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == NoteItemViewModel::class.java) {
           return  NoteItemViewModel(addNoteItemUseCase, editNoteItemUseCase, getNoteItemUseCase, noteDate) as T
        }
        throw RuntimeException("Unknown view model $modelClass")
    }
}