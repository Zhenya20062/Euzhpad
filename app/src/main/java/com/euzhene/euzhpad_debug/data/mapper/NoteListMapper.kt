package com.euzhene.euzhpad_debug.data.mapper

import com.euzhene.euzhpad_debug.data.database.NoteItemDbModel
import com.euzhene.euzhpad_debug.domain.entity.NoteItem
import javax.inject.Inject

class NoteListMapper @Inject constructor() {
    fun mapDbModelToEntity(noteItemDbModel: NoteItemDbModel): NoteItem =
        NoteItem(
            noteItemDbModel.id,
            noteItemDbModel.title,
            noteItemDbModel.content,
            noteItemDbModel.createDate,
            noteItemDbModel.lastEditTime,
            noteItemDbModel.password
        )

    fun mapDbModelListToEntity(noteItemDbList: List<NoteItemDbModel>): List<NoteItem> =
        noteItemDbList.map { mapDbModelToEntity(it) }

    fun mapEntityToDbModel(noteItem: NoteItem): NoteItemDbModel =
        NoteItemDbModel(
            noteItem.id,
            noteItem.title,
            noteItem.content,
            noteItem.createDate,
            noteItem.lastEditTime,
            noteItem.password
        )

}