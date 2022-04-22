package com.euzhene.euzhpad.presentation.note_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.euzhene.euzhpad.databinding.PartialNoteItemBinding
import com.euzhene.euzhpad.domain.entity.NoteItem

class NoteListAdapter : ListAdapter<NoteItem, NoteListViewHolder>(NoteListDiffCallback()) {
    var onItemClick: ((NoteItem) -> Unit)? = null
    var onItemLongClick: ((NoteItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {

        val binding = PartialNoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val noteItem = currentList[position]
        with(holder.binding) {
            tvNoteName.text = noteItem.title
            tvLastEditDate.text = noteItem.lastEditTime
            root.setOnClickListener {
                onItemClick?.invoke(noteItem)
            }

            root.setOnLongClickListener {
                onItemLongClick?.invoke(noteItem)
                true
            }
        }

    }
}