package com.euzhene.euzhpad_debug.presentation.note_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.euzhene.euzhpad_debug.R
import com.euzhene.euzhpad_debug.domain.entity.Filter
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import com.euzhene.euzhpad_debug.domain.usecase.DeleteNoteItemUseCase
import com.euzhene.euzhpad_debug.domain.usecase.EditNoteItemUseCase
import com.euzhene.euzhpad_debug.domain.usecase.GetDefaultFilterUseCase
import com.euzhene.euzhpad_debug.domain.usecase.GetNoteListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class NoteListViewModel(
    application: Application,
    private val editNoteItemUseCase: EditNoteItemUseCase,
    private val deleteNoteItemUseCase: DeleteNoteItemUseCase,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val getDefaultFilterUseCase: GetDefaultFilterUseCase,
) : AndroidViewModel(application) {
    var noteList: LiveData<List<NoteItem>> = getNoteListUseCase(Filter.BY_DEFAULT)
    var message: MutableSharedFlow<String?> = MutableSharedFlow()

    fun editNoteItem(noteItem: NoteItem, password: String?) {
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

    fun getDefaultFilter(): Filter {
        return getDefaultFilterUseCase()
    }

    fun exportNotes(notes: List<NoteItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var downloads = File("/storage/emulated/0/Download/")

                if (!downloads.exists()) downloads = File("/storage/emulated/0/Downloads/")
                val locale = getApplication<Application>().resources.configuration.locales[0]
                val dateString =
                    SimpleDateFormat("dd_MM_y", locale).format(Date())
                val fos = FileOutputStream("${downloads.path}/euzhpad_${notes.size}_notes_${dateString}.zip")
                val zos = ZipOutputStream(fos)
                notes.forEach {
                    val zipEntry = ZipEntry("${it.title}.txt")
                    zos.putNextEntry(zipEntry)
                    zos.write(it.content.toByteArray())
                    zos.closeEntry()
                }
                zos.close()
                message.emit(getApplication<Application>().getString(R.string.success_export_message, downloads.path))
            } catch (e: Exception) {
                message.emit(e.toString())
            }
        }
    }
}