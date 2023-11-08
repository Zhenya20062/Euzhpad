package com.euzhene.euzhpad_debug.di

import com.euzhene.euzhpad_debug.data.repository.NoteListRepositoryImpl
import com.euzhene.euzhpad_debug.domain.repository.NoteListRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    fun bindRepository(repository: NoteListRepositoryImpl):NoteListRepository
}