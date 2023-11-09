package com.euzhene.euzhpad_debug.presentation.note_list

import android.content.Intent
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

    open fun createDialog(): AlertDialog = createDefaultPasswordAlertDialog()

    protected fun createDefaultPasswordAlertDialog(
        btnText: String? = null,
        header: String? = null,
        onClick: ((AlertDialog) -> Unit)? = null
    ): AlertDialog {
        val _btnText = btnText ?: activity.getString(if (noteItem.hasPassword) R.string.unlock else R.string.lock)
        val _header = header ?: activity.getString(if (noteItem.hasPassword) R.string.settings_unlock else R.string.settings_lock)

        return buildPasswordAlertDialog(
            btnText = _btnText,
            header = _header,
            activity = activity,
            noteItem = noteItem,
            onCorrectPassword = { doAction() },
            onClick = onClick
        )
    }

    companion object {
        fun buildPasswordAlertDialog(
            btnText: String,
            header: String,
            activity: FragmentActivity,
            noteItem: NoteItem,
            onCorrectPassword: ((AlertDialog) -> Unit)? = null,
            onClick: ((AlertDialog) -> Unit)? = null
        ): AlertDialog {
            val alertDialog = AlertDialog.Builder(activity)
                .setView(R.layout.dialog_password)
                .setCancelable(true)
                .create()
            alertDialog.show()
            alertDialog.findViewById<Button>(R.id.btn_dialog_password)!!.apply {
                text = btnText
                setOnClickListener {
                    if (onClick != null) {
                        onClick(alertDialog)
                        return@setOnClickListener
                    }
                    val et = alertDialog.findViewById<EditText>(R.id.et_dialog_password)!!
                    val password = et.text.toString()
                    if (noteItem.password == password) {
                        if (onCorrectPassword != null) onCorrectPassword(alertDialog)
                        alertDialog.dismiss()
                    } else et.error = context.getString(R.string.incorrect_password)
                }

            }
            alertDialog.findViewById<TextView>(R.id.tv_dialog_password)!!.text = header
            return alertDialog
        }
    }
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

    override fun createDialog(): AlertDialog {
        return createDefaultPasswordAlertDialog {
            val et = it.findViewById<EditText>(R.id.et_dialog_password)!!
            val password = et.text.toString()

            if (!noteItem.hasPassword) {
                viewModel.editNoteItem(noteItem, password)
                it.dismiss()
            } else if (noteItem.password == password) {
                viewModel.editNoteItem(noteItem, null)
                it.dismiss()
            } else et.error = activity.getString(R.string.incorrect_password)
        }
    }
}