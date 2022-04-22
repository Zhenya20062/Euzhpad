package com.euzhene.euzhpad.di

import android.app.Application
import com.euzhene.euzhpad.presentation.MainActivity
import com.euzhene.euzhpad.presentation.NoteListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DomainModule::class, RoomModule::class])
interface AppComponent {
    fun inject(fragment: NoteListFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(
            @BindsInstance application:Application
        ):AppComponent
    }
}