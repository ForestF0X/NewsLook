package com.example.newslook.core.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint

// Easy to switch base activity in future
typealias BaseActivity = DaggerActivity

@AndroidEntryPoint
abstract class DaggerActivity : AppCompatActivity()
