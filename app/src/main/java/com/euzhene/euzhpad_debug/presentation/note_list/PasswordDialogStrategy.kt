package com.euzhene.euzhpad_debug.presentation.note_list

import android.content.Intent
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.euzhene.euzhpad_debug.R
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import com.euzhene.euzhpad_debug.presentation.note_item.NoteItemFragment

abstract class PasswordDialogStrategy(
    val noteItem: NoteItem,
    protected val activity: FragmentActivity,
) {

    abstract fun doAction()

    open fun createDialog(): PasswordDialog {
        return createDefaultPasswordAlertDialog().apply {
            onCorrectPassword = { doAction() }
        }
    }

    protected fun createDefaultPasswordAlertDialog(): PasswordDialog = PasswordDialog(activity = activity, noteItem = noteItem)

}

class EditNoteDialogStrategy(noteItem: NoteItem, activity: FragmentActivity) :
    PasswordDialogStrategy(noteItem, activity) {

    override fun doAction() {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, NoteItemFragment.newInstanceEditNote(noteItem.id))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}

class DeleteNoteDialogStrategy(noteItem: NoteItem, activity: FragmentActivity, private val viewModel: NoteListViewModel) :
    PasswordDialogStrategy(noteItem, activity) {

    override fun doAction() = viewModel.deleteNoteItem(noteItem)
}

class ExportNoteDialogStrategy(noteItem: NoteItem, activity: FragmentActivity, private val viewModel: NoteListViewModel) :
    PasswordDialogStrategy(noteItem, activity) {

    override fun doAction() = viewModel.exportNotes(listOf(noteItem))
}

class ShareNoteDialogStrategy(noteItem: NoteItem, activity: FragmentActivity, private val viewModel: NoteListViewModel) :
    PasswordDialogStrategy(noteItem, activity) {

    override fun doAction() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                viewModel.concatenateTitleAndContent(noteItem.title, noteItem.content)
            )
            type = "text/plain"
        }
        activity.startActivity(shareIntent)
    }
}

class LockNoteDialogStrategy(noteItem: NoteItem, activity: FragmentActivity, private val viewModel: NoteListViewModel) :
    PasswordDialogStrategy(noteItem, activity) {

    override fun doAction() {
        createDialog()
    }

    override fun createDialog(): PasswordDialog {
        val passwordDialog = createDefaultPasswordAlertDialog()
        passwordDialog.onClick = {
            val et = passwordDialog.findViewById<EditText>(R.id.et_dialog_password)!!
            val password = et.text.toString()

            if (!noteItem.hasPassword) {
                viewModel.editNoteItem(noteItem, password)
                passwordDialog.dismiss()
            } else if (noteItem.password == password) {
                viewModel.editNoteItem(noteItem, null)
                passwordDialog.dismiss()
            } else et.error = activity.getString(R.string.incorrect_password)
        }
        return passwordDialog
    }
}