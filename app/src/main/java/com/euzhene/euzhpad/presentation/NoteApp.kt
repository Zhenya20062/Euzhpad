package com.euzhene.euzhpad.presentation

import android.app.Application
import com.euzhene.euzhpad.di.AppComponent
import com.euzhene.euzhpad.di.DaggerAppComponent

class NoteApp:Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}