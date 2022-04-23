package com.euzhene.euzhpad.presentation.note_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.domain.usecase.AddNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteItemUseCase
import kotlinx.coroutines.launch

class NoteItemViewModel(
    private val addNoteItemUseCase: AddNoteItemUseCase,
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val getNoteItemUseCase: GetNoteItemUseCase,
    private val noteDate: NoteDate
) : ViewModel() {
    private val _noteItem = MutableLiveData<NoteItem>()
    val noteItem: LiveData<NoteItem>
        get() = _noteItem

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
                    finishWork()
                }
            }
        }
    }

    fun addNoteItem(inputTitle: String?, inputContent: String?) {
        val title = parseInput(inputTitle)
        val content = parseInput(inputContent)
        val fieldsCorrect = validateInput(title, content)
        if (fieldsCorrect) {
            val noteItem = NoteItem(
                title = title,
                content = content,
                lastEditTime = noteDate.getFullDate(System.currentTimeMillis())
            )
            viewModelScope.launch {
                addNoteItemUseCase(noteItem)
                finishWork()
            }
        }
    }

    fun getNoteItem(noteItemId: Int) {
        viewModelScope.launch {
            _noteItem.postValue(getNoteItemUseCase(noteItemId))
        }
    }

    fun concatenateTitleAndContent(title: String, content: String): String {
        val space = "\n\n"
        return title + space + content
    }


    private fun finishWork() {
        _shouldCloseScreen.postValue(Unit)
    }

    private fun validateInput(title: String, content: String): Boolean {
        return !(title.isBlank() || content.isBlank())
    }

    private fun parseInput(input: String?): String {
        return input?.trim() ?: ""
    }
}