package com.euzhene.euzhpad.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NoteListDao {
    @Query("select * from note_items")
    fun getNoteList(): LiveData<List<NoteItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteItem(noteItemDbModel: NoteItemDbModel)

    @Query("delete from note_items where id = :noteItemId")
    suspend fun deleteNoteItem(noteItemId: Int)
}