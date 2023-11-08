package com.euzhene.euzhpad_debug.presentation

import android.app.Application
import com.euzhene.euzhpad_debug.di.AppComponent
import com.euzhene.euzhpad_debug.di.DaggerAppComponent

class NoteApp:Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}