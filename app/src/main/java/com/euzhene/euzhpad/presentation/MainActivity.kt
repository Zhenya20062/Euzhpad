package com.euzhene.euzhpad.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.euzhene.euzhpad.databinding.ActivityMainBinding
import com.euzhene.euzhpad.di.AppComponent
import com.euzhene.euzhpad.di.DaggerAppComponent

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}