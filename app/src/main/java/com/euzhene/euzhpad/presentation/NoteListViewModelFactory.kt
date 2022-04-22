package com.euzhene.euzhpad.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.euzhene.euzhpad.domain.usecase.DeleteNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteListUseCase
import java.lang.RuntimeException
import javax.inject.Inject

class NoteListViewModelFactory @Inject constructor(
    private val application: Application,
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
    private val getNoteListUseCase: GetNoteListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == NoteListViewModel::class.java) {
            return NoteListViewModel(application, deleteNoteItemUseCase, getNoteListUseCase) as T
        }
        throw RuntimeException("Unknown view model $modelClass")
    }
}