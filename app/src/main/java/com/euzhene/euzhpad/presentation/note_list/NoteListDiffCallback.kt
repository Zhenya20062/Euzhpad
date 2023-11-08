package com.euzhene.euzhpad.presentation.note_list

import androidx.recyclerview.widget.DiffUtil
import com.euzhene.euzhpad.domain.entity.NoteItem

class NoteListDiffCallback:DiffUtil.ItemCallback<NoteItem>() {
    override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem == newItem
    }
}