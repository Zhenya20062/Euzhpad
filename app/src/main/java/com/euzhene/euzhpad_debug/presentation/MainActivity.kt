package com.euzhene.euzhpad_debug.presentation

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.euzhene.euzhpad_debug.databinding.ActivityMainBinding
import com.euzhene.euzhpad_debug.di.AppComponent
import com.euzhene.euzhpad_debug.presentation.preferences.PreferencesFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val component:AppComponent by lazy {
        ( application as NoteApp).component
    }
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)
        val themeId = sharedPreferences.getInt(
            PreferencesFragment.KEY_THEME,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
        AppCompatDelegate.setDefaultNightMode(themeId)
    }
}