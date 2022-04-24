package com.euzhene.euzhpad.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.euzhene.euzhpad.presentation.note_list.NoteListFragment
import dagger.Module
import dagger.Provides

@Module
class SharedPrefModule {
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val SHARED_PREF_NAME = "noteSharePref"
    }
}