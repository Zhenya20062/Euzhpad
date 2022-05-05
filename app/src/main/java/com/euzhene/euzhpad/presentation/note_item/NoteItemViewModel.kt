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

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _content = MutableLiveData<String>()
    val content: LiveData<String>
        get() = _content

    fun editNoteItem(inputTitle: String?, inputContent: String?) {
        val title = parseInput(inputTitle)
        val content = parseInput(inputContent)

        val fieldsChanged = title != noteItem.value?.title || content != noteItem.value?.content
        if (!fieldsChanged) {
            finishWork()
            return
        }

        val fieldsCorrect = title.isNotBlank()
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
                createDate = noteDate.getFullDate(System.currentTimeMillis()),
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
            val noteItem = getNoteItemUseCase(noteItemId)
            _noteItem.postValue(noteItem)
            _title.value = noteItem.title
            _content.value = noteItem.content

        }
    }

    fun updateNote(title: String, content: String) {
        _title.value = title
        _content.value = content
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