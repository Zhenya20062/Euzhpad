package com.euzhene.euzhpad.presentation.note_list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.ViewModelProvider
import com.euzhene.euzhpad.R
import com.euzhene.euzhpad.databinding.FragmentNoteListBinding
import com.euzhene.euzhpad.di.AppComponent
import com.euzhene.euzhpad.di.ExampleApp
import com.euzhene.euzhpad.domain.entity.NoteItem
import com.euzhene.euzhpad.presentation.note_item.NoteItemFragment
import javax.inject.Inject

class NoteListFragment : Fragment() {
    private var _binding: FragmentNoteListBinding? = null
    private val binding: FragmentNoteListBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteListBinding = null")

    private val component: AppComponent by lazy {
        (requireActivity().application as ExampleApp).component
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
    }

    private fun setupRecyclerView() {
        binding.rvNoteList.adapter = noteListAdapter
        noteListAdapter.onItemClick = {
            openEditMode(it)
        }
        noteListAdapter.onItemLongClick = {
            createAlertDialog(it).show()
        }
    }

    private fun createAlertDialog(noteItem: NoteItem): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setItems(R.array.alert_dialog_menu) { _: DialogInterface, i: Int ->
                when (resources.getStringArray(R.array.alert_dialog_menu)[i]) {
                    getString(R.string.settings_edit) -> {
                        openEditMode(noteItem)
                    }
                    getString(R.string.settings_delete) -> {
                        viewModel.deleteNoteItem(noteItem)
                    }
                    getString(R.string.settings_share) -> share(noteItem)
                    getString(R.string.settings_export) -> {
                        //todo
                    }
                    getString(R.string.settings_lock) -> {
                        //todo
                    }

                }
            }
            .setCancelable(true)
            .create()
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
            getString(R.string.filter) -> {
                //todo
            }
            getString(R.string.preferences) -> {
                //todo

            }
            getString(R.string.donate) -> {
                //todo

            }
            getString(R.string.about) -> {
                //todo

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TEXT_PLAIN_TYPE = "text/plain"
    }

}