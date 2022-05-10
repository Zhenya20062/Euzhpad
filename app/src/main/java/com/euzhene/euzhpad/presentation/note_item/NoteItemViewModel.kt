package com.euzhene.euzhpad.presentation.note_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.domain.usecase.AddNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad.domain.usecase.GetNoteItemUseCase
import com.euzhene.euzhpad.presentation.util.getFullDate
import kotlinx.coroutines.launch

class NoteItemViewModel(
    private val addNoteItemUseCase: AddNoteItemUseCase,
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val getNoteItemUseCase: GetNoteItemUseCase,
) : ViewModel() {
    private val _noteItem = MutableLiveData<NoteItem>()
    val noteItem: LiveData<NoteItem>
        get() = _noteItem

    private val _successfullySaved = MutableLiveData<Unit>()
    val successfullySaved: LiveData<Unit>
        get() = _successfullySaved

    private val _fieldsEmpty = MutableLiveData<Unit>()
    val fieldsEmpty: LiveData<Unit>
        get() = _fieldsEmpty

    private val _fieldsNotChanged = MutableLiveData<Unit>()
    val fieldsNotChanged: LiveData<Unit>
        get() = _fieldsNotChanged

    private val _title = MutableLiveData<String>("")
    val title: LiveData<String>
        get() = _title

    private val _content = MutableLiveData<String>("")
    val content: LiveData<String>
        get() = _content

    private fun validateInputState(inputTitle: String, inputContent: String):Boolean {
        val title = parseInput(inputTitle)
        val content = parseInput(inputContent)

        val fieldsChanged = title != noteItem.value?.title || content != noteItem.value?.content
        if (!fieldsChanged) {
            _fieldsNotChanged.value = Unit
            return false
        }

        val fieldsCorrect = validateInput(title, content)
        if (!fieldsCorrect) {
            _fieldsEmpty.value = Unit
            return false
        }
        return true
    }
    fun editNoteItem(inputTitle: String, inputContent: String) {
        if (validateInputState(inputTitle, inputContent)) {
            _noteItem.value?.let {
                val noteItem = it.copy(
                    title = parseInput(inputTitle),
                    content = parseInput(inputContent),
                    lastEditTime = getFullDate(System.currentTimeMillis())
                )
                viewModelScope.launch {
                    editNoteItemUseCase(noteItem)
                    _successfullySaved.value = Unit
                }
            }
        }
    }

    fun addNoteItem(inputTitle: String, inputContent: String) {
        if (validateInputState(inputTitle, inputContent)) {
            val noteItem = NoteItem(
                title = parseInput(inputTitle),
                content = parseInput(inputContent),
                createDate = getFullDate(System.currentTimeMillis()),
                lastEditTime = getFullDate(System.currentTimeMillis())
            )
            viewModelScope.launch {
                addNoteItemUseCase(noteItem)
                _successfullySaved.value = Unit
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

    fun noteNotChanged(title: String, content: String):Boolean {
        val  titleNotChanged = title == _title.value
        val contentNotChanged = content == _content.value
        return titleNotChanged && contentNotChanged
    }


    private fun validateInput(title: String, content: String): Boolean {
        return !(title.isBlank() && content.isBlank())
    }

    private fun parseInput(input: String): String {
        return input.trim()
    }
}