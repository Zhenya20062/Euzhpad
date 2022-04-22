package com.euzhene.euzhpad.di

import android.app.Application
import com.euzhene.euzhpad.presentation.edit_note.EditItemFragment
import com.euzhene.euzhpad.presentation.note_list.NoteListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class, RoomModule::class])
interface AppComponent {
    fun inject(fragment: NoteListFragment)
    fun inject(fragment:EditItemFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(
            @BindsInstance application:Application
        ):AppComponent
    }
}