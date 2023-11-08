package com.euzhene.euzhpad.di

import android.app.Application
import com.euzhene.euzhpad.data.database.AppDatabase
import com.euzhene.euzhpad.data.database.NoteListDao
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