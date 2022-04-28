package com.euzhene.euzhpad.presentation

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.euzhene.euzhpad.databinding.ActivityMainBinding
import com.euzhene.euzhpad.di.AppComponent
import com.euzhene.euzhpad.di.DaggerAppComponent
import com.euzhene.euzhpad.di.ExampleApp
import com.euzhene.euzhpad.presentation.preferences.PreferencesFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val component:AppComponent by lazy {
        ( application as ExampleApp).component
    }
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val themeId = sharedPreferences.getInt(
            PreferencesFragment.KEY_THEME,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
        AppCompatDelegate.setDefaultNightMode(themeId)
    }
}