package com.euzhene.euzhpad_debug.presentation.preferences

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import com.euzhene.euzhpad_debug.R
import com.euzhene.euzhpad_debug.di.AppComponent
import com.euzhene.euzhpad_debug.presentation.NoteApp
import javax.inject.Inject

class PreferencesFragment : PreferenceFragmentCompat() {
    private val component: AppComponent by lazy {
        (requireActivity().application as NoteApp).component
    }

    @Inject
    lateinit var sharedPreference: SharedPreferences

    private val cbPref: CheckBoxPreference by lazy {
        preferenceManager.findPreference<CheckBoxPreference>("dark theme")
            ?: throw RuntimeException("Unable to find this check box")
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onStart() {
        component.inject(this)
        super.onStart()
        getPrefs()
        checkState()
    }

    private fun getPrefs() {
        cbPref.setOnPreferenceClickListener {
            val nightMode = AppCompatDelegate.getDefaultNightMode()
            val themeId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.setDefaultNightMode(themeId)
            sharedPreference.edit().putInt(KEY_THEME, themeId).apply()

            true
        }

    }

    private fun checkState() {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        cbPref.isChecked = nightMode == AppCompatDelegate.MODE_NIGHT_YES
    }

    companion object {
        fun newIntent() = PreferencesFragment()

        const val KEY_THEME = "theme"
    }

}