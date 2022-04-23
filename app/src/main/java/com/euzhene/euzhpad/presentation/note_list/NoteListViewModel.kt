package com.euzhene.euzhpad.presentation.note_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.domain.usecase.DeleteNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteListUseCase
import kotlinx.coroutines.launch

class NoteListViewModel(
    application: Application,
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
    getNoteListUseCase: GetNoteListUseCase
) : AndroidViewModel(application) {
    var noteList: LiveData<List<NoteItem>> = getNoteListUseCase()

    fun deleteNoteItem(noteItem: NoteItem) {
        viewModelScope.launch {
            deleteNoteItemUseCase(noteItem)
        }
    }

    fun concatenateTitleAndContent(title: String, content: String): String {
        val space = "\n\n"
        return title + space + content
    }
}