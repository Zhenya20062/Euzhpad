package com.euzhene.euzhpad.presentation.edit_note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteItemUseCase
import kotlinx.coroutines.launch

class EditItemViewModel(
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val getNoteItemUseCase: GetNoteItemUseCase,
    private val noteDate: NoteDate
) : ViewModel() {
    private val _noteItem = MutableLiveData<NoteItem>()
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen


    fun editNoteItem(inputTitle: String?, inputContent: String?) {
        val title = parseInput(inputTitle)
        val content = parseInput(inputContent)
        val fieldsCorrect = validateInput(title, content)
        if (fieldsCorrect) {
            _noteItem.value?.let {
                val noteItem = it.copy(
                    title = title,
                    content = content,
                    lastEditTime = noteDate.getFullDate(System.currentTimeMillis())
                )
                viewModelScope.launch {
                    editNoteItemUseCase(noteItem)
                    _shouldCloseScreen.postValue(Unit)
                }
            }
        }

    }

    fun getNoteItem(noteItemId: Int) {
        viewModelScope.launch {
            _noteItem.postValue(getNoteItemUseCase(noteItemId))
        }
    }

    private fun validateInput(title: String, content: String): Boolean {
        return !(title.isBlank() || content.isBlank())
    }

    private fun parseInput(input: String?): String {
        return input?.trim() ?: ""
    }
}