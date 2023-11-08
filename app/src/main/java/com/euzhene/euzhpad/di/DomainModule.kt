package com.euzhene.euzhpad.di

import com.euzhene.euzhpad.data.repository.NoteListRepositoryImpl
import com.euzhene.euzhpad.domain.repository.NoteListRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    fun bindRepository(repository: NoteListRepositoryImpl):NoteListRepository
}