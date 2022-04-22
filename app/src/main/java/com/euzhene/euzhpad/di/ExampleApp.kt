package com.euzhene.euzhpad.di

import android.app.Application

class ExampleApp:Application() {
    val component:AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}