package com.euzhene.euzhpad_debug.presentation.note_item

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.euzhene.euzhpad_debug.R
import com.euzhene.euzhpad_debug.databinding.FragmentNoteEditBinding
import com.euzhene.euzhpad_debug.di.AppComponent
import com.euzhene.euzhpad_debug.presentation.NoteApp
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import javax.inject.Inject

class NoteItemFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding: FragmentNoteEditBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteEditBinding = null")

    private var noteItemId: Int = NoteItem.UNKNOWN_ID
    private var screenMode = UNKNOWN_MODE
    private val component: AppComponent by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var viewModelFactory: NoteItemViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NoteItemViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(
            binding.editNoteToolbar as Toolbar
        )
        if (savedInstanceState == null) {
            when (screenMode) {
                EDIT_MODE_VALUE -> {
                    viewModel.getNoteItem(noteItemId)
                }
            }

        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setBackPressCallback()
        observeViewModel()
        setTextChangedListener()
    }

    private fun setTextChangedListener() {
        binding.etTitle.addTextChangedListener {
            viewModel.updateNote(it.toString(), binding.etContent.text.toString())
        }
        binding.etContent.addTextChangedListener {
            viewModel.updateNote(binding.etTitle.text.toString(), it.toString())
        }
    }

    private fun setBackPressCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val title = binding.etTitle.text.toString()
                val content = binding.etContent.text.toString()

                if (!viewModel.noteNotChanged()) {
                    val okayListener = DialogInterface.OnClickListener { _, _ ->
                        if (title.isBlank() && content.isBlank()) close()
                        else save()
                    }
                    val cancelListener = DialogInterface.OnClickListener { _, _ -> close() }

                    AlertDialog.Builder(requireContext())
                        .setCancelable(true)
                        .setTitle(R.string.save_note_title)
                        .setMessage(R.string.save_note_message)
                        .setPositiveButton(R.string.okay, okayListener)
                        .setNegativeButton(R.string.cancel, cancelListener)
                        .create()
                        .show()
                } else {
                    close()
                }

            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun close() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun observeViewModel() {
        viewModel.successfullySaved.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
        viewModel.fieldsEmpty.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.empty_note, Toast.LENGTH_SHORT).show()
        }
        viewModel.fieldsNotChanged.observe(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewModel.foundIndex.observe(viewLifecycleOwner) {
            Log.i("found index", "observeViewModel: $it")
            highlightContentString(it.first, it.second)
            binding.etContent.setSelection(it.first)
            binding.etContent.requestFocus()
        }
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(MODE_ARG)) {
            throw RuntimeException("Argument mode is absent")
        }
        val mode = args.getString(MODE_ARG) ?: throw RuntimeException("Argument mode is absent")
        if (mode != NEW_NOTE_MODE_VALUE && mode != EDIT_MODE_VALUE) {
            throw RuntimeException("Unknown mode $mode")
        }
        screenMode = mode
        if (mode == EDIT_MODE_VALUE) {
            if (!args.containsKey(NOTE_ITEM_ID_ARG)) {
                throw RuntimeException("Note item arg is absent")
            }
            noteItemId = args.getInt(NOTE_ITEM_ID_ARG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.edit_note_menu, menu)
        val searchView = (menu.findItem(R.id.note_edit_search).actionView as SearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) viewModel.updateSearchText(binding.etContent.text.toString(), query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            getString(R.string.save) -> save()
            getString(R.string.share) -> share()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun share() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                viewModel.concatenateTitleAndContent(title, content)
            )
            type = TEXT_PLAIN_TYPE
        }
        startActivity(shareIntent)
    }

    private fun save() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        when (screenMode) {
            EDIT_MODE_VALUE -> viewModel.editNoteItem(title, content)
            NEW_NOTE_MODE_VALUE -> viewModel.addNoteItem(title, content)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        viewModel.updateNote(
//            binding.etTitle.text.toString(),
//            binding.etContent.text.toString()
//        )
//
//    }

    private fun highlightContentString(start: Int, end: Int) {
        binding.etContent.text.clearSpans()
        val spannable: Spannable = SpannableString(binding.etContent.text)
        spannable.setSpan(BackgroundColorSpan(resources.getColor(R.color.purple_200, requireContext().theme)), start, end, Spannable
            .SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.etContent.setText(spannable)
    }

    companion object {
        private const val NOTE_ITEM_ID_ARG = "note_item_id"
        private const val MODE_ARG = "mode"
        private const val EDIT_MODE_VALUE = "edit_mode"
        private const val NEW_NOTE_MODE_VALUE = "new_note_mode"
        private const val UNKNOWN_MODE = ""
        fun newInstanceEditNote(noteItemId: Int): NoteItemFragment =
            NoteItemFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE_ARG, EDIT_MODE_VALUE)
                    putInt(NOTE_ITEM_ID_ARG, noteItemId)
                }
            }

        fun newInstanceAddNote(): NoteItemFragment =
            NoteItemFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE_ARG, NEW_NOTE_MODE_VALUE)
                }
            }

        private const val TEXT_PLAIN_TYPE = "text/plain"
    }
}