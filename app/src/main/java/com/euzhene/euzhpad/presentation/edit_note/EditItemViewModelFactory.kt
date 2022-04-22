package com.euzhene.euzhpad.presentation.edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.euzhene.euzhpad.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteItemUseCase
import java.lang.RuntimeException
import javax.inject.Inject

class EditItemViewModelFactory @Inject constructor(
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val getNoteItemUseCase: GetNoteItemUseCase,
    private val noteDate: NoteDate
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == EditItemViewModel::class.java) {
           return  EditItemViewModel(editNoteItemUseCase, getNoteItemUseCase, noteDate) as T
        }
        throw RuntimeException("Unknown view model $modelClass")
    }
}