package com.euzhene.euzhpad_debug.presentation.note_list

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.euzhene.euzhpad_debug.R
import com.euzhene.euzhpad_debug.databinding.DialogPasswordBinding
import com.euzhene.euzhpad_debug.domain.entity.NoteItem


class PasswordDialog(
    private val activity: FragmentActivity,
    private val noteItem: NoteItem,
    private val btnText: String? = null,
    private val header: String? = null
) : AlertDialog(activity) {
    var onCorrectPassword: (() -> Unit)? = null
    var onClick: (() -> Unit)? = null

    private lateinit var binding: DialogPasswordBinding

    init {
        show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        this.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) //to make keyboard appear

        binding.btnDialogPassword.apply {
            text = btnText ?: activity.getString(if (noteItem.hasPassword) R.string.unlock else R.string.lock)
            setOnClickListener {
                onClick?.invoke()
                if (onClick != null) return@setOnClickListener

                val password = binding.etDialogPassword.text.toString()
                if (noteItem.password == password) {
                    onCorrectPassword?.invoke()
                    dismiss()
                } else binding.etDialogPassword.error = context.getString(R.string.incorrect_password)
            }

        }
        binding.tvDialogPassword.text =
            header ?: activity.getString(if (noteItem.hasPassword) R.string.settings_unlock else R.string.settings_lock)
    }
}