package com.euzhene.euzhpad.di

import android.app.Application
import com.euzhene.euzhpad.presentation.MainActivity
import com.euzhene.euzhpad.presentation.note_item.NoteItemFragment
import com.euzhene.euzhpad.presentation.note_list.NoteListFragment
import com.euzhene.euzhpad.presentation.preferences.PreferencesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class, RoomModule::class, SharedPrefModule::class])
interface AppComponent {
    fun inject(fragment: NoteListFragment)
    fun inject(fragment:NoteItemFragment)
    fun inject(fragment: PreferencesFragment)
    fun inject(activity:MainActivity)

    @Component.Factory
    interface AppComponentFactory {
        fun create(
            @BindsInstance application:Application
        ):AppComponent
    }
}