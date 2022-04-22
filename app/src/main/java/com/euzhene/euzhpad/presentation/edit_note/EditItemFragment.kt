package com.euzhene.euzhpad.presentation.edit_note

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.euzhene.euzhpad.R
import com.euzhene.euzhpad.databinding.FragmentNoteEditBinding
import com.euzhene.euzhpad.di.AppComponent
import com.euzhene.euzhpad.di.ExampleApp
import com.euzhene.euzhpad.domain.entity.NoteItem
import java.lang.RuntimeException
import javax.inject.Inject

class EditItemFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding: FragmentNoteEditBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteEditBinding = null")

    private lateinit var noteItem: NoteItem
    private val component:AppComponent by lazy {
        (requireActivity().application as ExampleApp).component
    }

    @Inject
    lateinit var viewModelFactory: EditItemViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EditItemViewModel::class.java]
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
            binding.editNoteToolbar
                    as Toolbar
        )
        binding.noteItem = noteItem
        viewModel.getNoteItem(noteItem.id)
        observeViewModel()
    }
    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            requireActivity().onBackPressed()
        }
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(NOTE_ITEM_ARG)) {
            throw RuntimeException("Note item arg is null")
        }

        noteItem = args.getParcelable(NOTE_ITEM_ARG)
            ?: throw RuntimeException("Note item arg is null")

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.edit_note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            getString(R.string.save) -> save()
            getString(R.string.share) -> {}//todo
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        viewModel.editNoteItem(
            binding.etTitle.text.toString(),
            binding.etContent.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NOTE_ITEM_ARG = "note_item"
        fun newInstance(noteItem: NoteItem): EditItemFragment =
            EditItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(NOTE_ITEM_ARG, noteItem)
                }
            }
    }
}