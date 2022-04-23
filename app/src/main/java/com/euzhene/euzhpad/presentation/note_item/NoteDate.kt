package com.euzhene.euzhpad.presentation.note_item

import android.icu.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteDate @Inject constructor(){
    fun getFullDate(timeMillis:Long):String {
        val date = Date(timeMillis)
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
        return sdf.format(date)
    }
}