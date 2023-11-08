package com.euzhene.euzhpad_debug.presentation.note_list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.euzhene.euzhpad_debug.R
import com.euzhene.euzhpad_debug.databinding.FragmentNoteListBinding
import com.euzhene.euzhpad_debug.di.AppComponent
import com.euzhene.euzhpad_debug.domain.entity.Filter
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import com.euzhene.euzhpad_debug.presentation.NoteApp
import com.euzhene.euzhpad_debug.presentation.note_item.NoteItemFragment
import com.euzhene.euzhpad_debug.presentation.preferences.PreferencesFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteListFragment : Fragment() {
    private var _binding: FragmentNoteListBinding? = null
    private val binding: FragmentNoteListBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteListBinding = null")

    private val component: AppComponent by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: NoteListViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NoteListViewModel::class.java]
    }
    private val noteListAdapter = NoteListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        component.inject(this)
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.include as androidx.appcompat.widget.Toolbar)
        setupRecyclerView()

        viewModel.noteList.observe(viewLifecycleOwner) {
            noteListAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.message.collect {
                if (it == null) return@collect;
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setupRecyclerView() {
        binding.rvNoteList.adapter = noteListAdapter
        noteListAdapter.onItemClick = {
            passPassword(it, R.string.settings_edit)
        }
        noteListAdapter.onItemLongClick = {
            createOptionsAlertDialog(it).show()
        }
    }

    private fun createOptionsAlertDialog(noteItem: NoteItem): AlertDialog {
        val options = if (noteItem.password.isNullOrEmpty()) {
            R.array.alert_dialog_menu_lock
        } else {
            R.array.alert_dialog_menu_unlock
        }

        return AlertDialog.Builder(requireContext())
            .setItems(options, createDialogListener(noteItem))
            .setCancelable(true)
            .create()
    }

    private fun createDialogListener(noteItem: NoteItem): DialogInterface.OnClickListener {

        return DialogInterface.OnClickListener { _, which ->
            when (resources.getStringArray(R.array.alert_dialog_menu_lock)[which]) {
                getString(R.string.settings_edit) -> passPassword(noteItem, R.string.settings_edit)
                getString(R.string.settings_delete) -> passPassword(noteItem, R.string.settings_delete)
                getString(R.string.settings_share) -> passPassword(noteItem, R.string.settings_share)
                getString(R.string.settings_export) -> {
                    //todo
                }

                getString(R.string.settings_lock) -> changePassword(noteItem)
            }
        }
    }

    private fun createPasswordAlertDialog(password: Boolean): AlertDialog {
        val v: Int = if (password) {
            R.layout.alert_dialog_lock_with_password
        } else {
            R.layout.alert_dialog_unlock_by_password
        }
        return AlertDialog.Builder(requireContext())
            .setView(v)
            .setCancelable(true)
            .create()
    }

    private fun changePassword(noteItem: NoteItem) {
        val alertDialog = createPasswordAlertDialog(noteItem.password.isNullOrEmpty())
            .apply { show() }

        val et = alertDialog.findViewById<EditText>(R.id.et_password)
        val btn = alertDialog.findViewById<Button>(R.id.btn_lock)
            ?: alertDialog.findViewById<Button>(R.id.btn_unlock)

        btn?.setOnClickListener {
            val password = et?.text.toString()
            if (noteItem.password.isNullOrEmpty()) {
                viewModel.editNoteItem(noteItem, password)
            } else {
                if (noteItem.password == password) {
                    viewModel.editNoteItem(noteItem, null)
                }
            }
            alertDialog.dismiss()
        }
    }

    private fun share(noteItem: NoteItem) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                viewModel.concatenateTitleAndContent(noteItem.title, noteItem.content)
            )
            type = TEXT_PLAIN_TYPE
        }
        startActivity(shareIntent)
    }

    private fun openEditMode(noteItem: NoteItem) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, NoteItemFragment.newInstanceEditNote(noteItem.id))
            .addToBackStack(null)
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun passPassword(noteItem: NoteItem, stringId: Int) {
        if (noteItem.password.isNullOrEmpty()) {
            when (stringId) {
                R.string.settings_edit -> openEditMode(noteItem)
                R.string.settings_delete -> viewModel.deleteNoteItem(noteItem)
                R.string.settings_share -> share(noteItem)
            }
        } else {
            val alertDialog = createPasswordAlertDialog(noteItem.password.isEmpty())
                .apply { show() }

            val et: EditText = alertDialog.findViewById(R.id.et_password)
                ?: throw java.lang.RuntimeException("Couldn't find the right edit text")
            val btn: Button = alertDialog.findViewById(R.id.btn_lock)
                ?: alertDialog.findViewById(R.id.btn_unlock)
                ?: throw java.lang.RuntimeException("Couldn't find the right button")

            btn.setOnClickListener {
                val password = et.text.toString()

                if (noteItem.password == password) {
                    when (stringId) {
                        R.string.settings_edit -> openEditMode(noteItem)
                        R.string.settings_delete -> viewModel.deleteNoteItem(noteItem)
                        R.string.settings_share -> share(noteItem)
                    }
                    alertDialog.dismiss()
                } else {
                    et.error = getString(R.string.incorrect_password)
                }
            }
        }
    }

    private fun openNewNoteMode() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, NoteItemFragment.newInstanceAddNote())
            .addToBackStack(null)
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.note_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            getString(R.string.new_note) -> openNewNoteMode()
            getString(R.string.filter) -> sort()
            getString(R.string.preferences) -> openPreferencesFragment()
            getString(R.string.export_notes) -> exportNotes();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exportNotes() {
        val notes = viewModel.noteList.value?.filter { it.password != null }?.toMutableList() ?: return
        showPasswordAlertDialog(notes)
    }

    private fun showPasswordAlertDialog(notes: MutableList<NoteItem>) {
        /*logic: if the password the user enters for a note is correct - go to another note until
         all the notes with password are gone. After entering all needing passwords we can proceed and export them
         */
        if (notes.isEmpty()) {
            viewModel.exportNotes(viewModel.noteList.value ?: listOf())
            return
        }
        val note = notes.first()

        val alertDialog = createExportAlertDialog().apply { show() }
        val text = getString(R.string.enter_password_for, note.title)
        alertDialog.findViewById<TextView>(R.id.tv_enter_password_for)!!.text = text
        alertDialog.findViewById<Button>(R.id.btn_next)!!.setOnClickListener {

            val editText = alertDialog.findViewById<EditText>(R.id.et_password)!!
            if (editText.text.toString() == note.password) {
                alertDialog.dismiss()
                showPasswordAlertDialog(notes.apply { removeAt(0)})
            }
            else editText.error = getString(R.string.incorrect_password)
        }
    }

    private fun openPreferencesFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, PreferencesFragment.newIntent())
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun sort() {
        val alertDialog = createSortingAlertDialog().apply { show() }

        val radioGroup: RadioGroup = alertDialog.findViewById(R.id.radio_group_sorting)
            ?: throw RuntimeException("Couldn't find the right radioGroup")
        val radioBtnId = when (viewModel.getDefaultFilter()) {
            Filter.BY_TITLE -> R.id.rb_title
            Filter.BY_CHANGE_DATE -> R.id.rb_change_date
            Filter.BY_CREATE_DATE -> R.id.rb_create_date
            else -> throw RuntimeException("Impossible input")
        }
        radioGroup.check(radioBtnId)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = alertDialog.findViewById(checkedId) as? RadioButton
                ?: throw RuntimeException("Couldn't find the right radioButton")

            val filter: Filter = when (val radioButtonText = radioButton.text.toString()) {
                getString(R.string.by_title) -> Filter.BY_TITLE
                getString(R.string.by_create_date) -> Filter.BY_CREATE_DATE
                getString(R.string.by_change_date) -> Filter.BY_CHANGE_DATE
                else -> throw java.lang.RuntimeException("Unknown type $radioButtonText")
            }
            viewModel.getNoteListByFilter(filter)
            alertDialog.dismiss()
        }

    }

    private fun createSortingAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.alert_dialog_sorting)
            .setCancelable(true)
            .create()
    }

    private fun createExportAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.alert_dialog_export_with_password)
            .setCancelable(true)
            .create()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TEXT_PLAIN_TYPE = "text/plain"
    }

}