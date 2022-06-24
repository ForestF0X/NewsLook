package com.example.newslook.news.ui.activity

import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslook.core.ui.ViewState
import com.example.newslook.core.ui.base.BaseActivity
import com.example.newslook.core.utils.observeNotNull
import com.example.newslook.databinding.ActivityMainBinding
import com.example.newslook.news.ui.adapter.NewsArticlesAdapter
import com.example.newslook.news.ui.viewmodel.NewsArticleViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NewsActivity : BaseActivity() {

    private val newsArticleViewModel: NewsArticleViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: NewsArticlesAdapter

    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var theme: String

    var listThemes: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.currendDateText.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        // Setting up Spinner and adapter
        newsArticleViewModel.prePopulateList()
        listThemes = newsArticleViewModel.getListThemes()
        // Create an ArrayAdapter using a simple spinner layout and languages array
        arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, listThemes)
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.listThemes.adapter = arrayAdapter
        binding.listThemes.adapter.hasStableIds()
        binding.listThemes.setSelection(0,false)
        binding.listThemes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                    theme = listThemes[position]
                    updateGUI()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        // Setting up RecyclerView and adapter
        binding.newsList.setEmptyView(binding.emptyLayout.emptyView)
        binding.newsList.setProgressView(binding.progressLayout.progressView)
        adapter = NewsArticlesAdapter { position ->
            onItemClick(position)
        }
        binding.newsList.adapter = adapter
        binding.newsList.layoutManager = LinearLayoutManager(this)
        updateGUI()
        binding.editThemes.setOnClickListener {
            showDialog()
        }
    }

    override fun onStop() {
        adapter = NewsArticlesAdapter {  }
        super.onStop()
    }

    override fun onPause() {
        adapter = NewsArticlesAdapter {  }
        super.onPause()
    }

    private fun onItemClick(position: Int) {
        val url = adapter.currentList[position].url
        val newsIntent = Intent(this, WebViewActivity::class.java)
        newsIntent.putExtra("url", url)
        newsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(newsIntent)
    }

    fun updateGUI(){
        val pos =  binding.listThemes.selectedItemPosition
        theme = listThemes[pos]
        newsArticleViewModel.saveData(theme)
        binding.newsList.isVisible = false
        Thread.sleep(500)
        newsArticleViewModel.getNewsArticles().observeNotNull(this) { state ->
            when (state) {
                is ViewState.Success -> {
                    adapter.submitList(state.data)
                    adapter.notifyDataSetChanged()
                    binding.newsList.isVisible = true
                }
                is ViewState.Loading -> {
                    binding.newsList.showLoading()
                }
            }
        }
    }

    private fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Категории")

        val text = TextView(this)
        text.text = "Какое действие с категориями вы хотите совершить?"
        builder.setView(text)
        // Set up the buttons
        builder.setPositiveButton("Добавить", DialogInterface.OnClickListener { dialog, which ->
            showAddDialog()
        })
        builder.setNegativeButton("Удалить", DialogInterface.OnClickListener { dialog, which ->
            showRemoveDialog()
        })
        builder.setNeutralButton("Отмена", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })

        builder.show()
    }

    private fun showAddDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Добавление категории")

        // Set up the input
        val input = EditText(this)
        input.hint = "Введите название новой категории"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            newsArticleViewModel.addListThemes(input.text.toString())
            arrayAdapter.notifyDataSetChanged()
        })
        builder.setNegativeButton("Отмена", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun showRemoveDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Удаление категории")

        // Set up the input
        val input = EditText(this)
        input.hint = "Введите название удаляемой категории"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            newsArticleViewModel.removeListThemes(input.text.toString())
            arrayAdapter.notifyDataSetChanged()
        })
        builder.setNegativeButton("Отмена", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

}
