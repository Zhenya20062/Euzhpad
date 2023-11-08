package com.euzhene.euzhpad.presentation.util

import android.icu.text.SimpleDateFormat
import java.util.*

fun getFullDate(timeMillis: Long): String {
    val date = Date(timeMillis)
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
    return sdf.format(date)
}
