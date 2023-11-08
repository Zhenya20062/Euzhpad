package com.euzhene.euzhpad_debug.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteListDao {
    @Query("select * from note_items order by title asc")
    fun getNoteListByTitle(): LiveData<List<NoteItemDbModel>>

    @Query("select * from note_items order by createDate desc")
    fun getNoteListByCreateDate(): LiveData<List<NoteItemDbModel>>

    @Query("select * from note_items order by lastEditTime desc")
    fun getNoteListByLastEditTime(): LiveData<List<NoteItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteItem(noteItemDbModel: NoteItemDbModel)

    @Query("delete from note_items where id = :noteItemId")
    suspend fun deleteNoteItem(noteItemId: Int)

    @Query("select * from note_items where id = :noteItemId limit 1")
    suspend fun getNoteItemById(noteItemId: Int): NoteItemDbModel
}