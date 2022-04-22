package com.euzhene.euzhpad.presentation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.euzhene.euzhpad.R
import com.euzhene.euzhpad.databinding.FragmentNoteEditBinding
import com.euzhene.euzhpad.domain.entity.NoteItem
import java.lang.RuntimeException

class EditItemFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding: FragmentNoteEditBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteEditBinding = null")

    private lateinit var noteItem: NoteItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.editNoteToolbar
                as Toolbar)
        binding.noteItem = noteItem
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