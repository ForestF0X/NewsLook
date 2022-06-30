package com.example.newslook.news.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.newslook.R
import com.example.newslook.core.ui.base.BaseActivity
import com.example.newslook.databinding.ActivityMainBinding
import com.example.newslook.news.ui.viewmodel.NewsArticleViewModel


class NewsActivity : BaseActivity() {

    private val newsArticleViewModel: NewsArticleViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsArticleViewModel.prePopulateList()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_test)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_test)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}