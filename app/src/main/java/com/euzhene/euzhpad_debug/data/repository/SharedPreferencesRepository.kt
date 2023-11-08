package com.euzhene.euzhpad_debug.data.repository

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    val filter: String
        get() = sharedPreferences.getString(FILTER_KEY, DEFAULT_FILTER_VALUE)
            ?: DEFAULT_FILTER_VALUE

    fun updateFilterValue(filter: String) {
        sharedPreferences.edit().putString(FILTER_KEY, filter).apply()
    }


    companion object {
        private const val FILTER_KEY = "filter"
        private const val DEFAULT_FILTER_VALUE = "title"
    }
}