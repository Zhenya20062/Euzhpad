package com.euzhene.euzhpad.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_items")
data class NoteItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = UNKNOWN_ID,
    val title: String,
    val content: String,
    val lastEditTime: String,
    val password:String? = null,
) {
    companion object {
        const val UNKNOWN_ID = 0
    }
}