package com.euzhene.euzhpad_debug.di

import android.app.Application
import com.euzhene.euzhpad_debug.data.database.AppDatabase
import com.euzhene.euzhpad_debug.data.database.NoteListDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule() {

    @Singleton
    @Provides
    fun provideRoomDatabase(application: Application): AppDatabase =
        AppDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideDao(appDatabase: AppDatabase): NoteListDao = appDatabase.noteListDao()

}