package com.euzhene.euzhpad.presentation.note_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.euzhene.euzhpad.domain.usecase.DeleteNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetDefaultFilterUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteListUseCase
import java.lang.RuntimeException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class NoteListViewModelFactory @Inject constructor(
    private val application: Application,
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val getDefaultFilterUseCase: GetDefaultFilterUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == NoteListViewModel::class.java) {
            return NoteListViewModel(
                application,
                editNoteItemUseCase,
                deleteNoteItemUseCase,
                getNoteListUseCase,
                getDefaultFilterUseCase,
            ) as T
        }
        throw RuntimeException("Unknown view model $modelClass")
    }
}