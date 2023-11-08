package com.euzhene.euzhpad_debug.presentation.note_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.euzhene.euzhpad_debug.domain.entity.Filter
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import com.euzhene.euzhpad_debug.domain.usecase.DeleteNoteItemUseCase
import com.euzhene.euzhpad_debug.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad_debug.domain.usecase.GetDefaultFilterUseCase
import com.euzhene.euzhpad_debug.domain.usecase.GetNoteListUseCase
import kotlinx.coroutines.launch

class NoteListViewModel(
    application: Application,
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val getDefaultFilterUseCase: GetDefaultFilterUseCase,
) : AndroidViewModel(application) {
    var noteList: LiveData<List<NoteItem>> = getNoteListUseCase(Filter.BY_DEFAULT)

    fun editNoteItem(noteItem: NoteItem, password:String?) {
        val item = noteItem.copy(password = password)
        viewModelScope.launch {
            editNoteItemUseCase(item)
        }
    }

    fun deleteNoteItem(noteItem: NoteItem) {
        viewModelScope.launch {
            deleteNoteItemUseCase(noteItem)
        }
    }

    fun concatenateTitleAndContent(title: String, content: String): String {
        val space = "\n\n"
        return title + space + content
    }

    fun getNoteListByFilter(filter: Filter) {
        getNoteListUseCase(filter)
    }
    fun getDefaultFilter():Filter {
        return getDefaultFilterUseCase()
    }


}