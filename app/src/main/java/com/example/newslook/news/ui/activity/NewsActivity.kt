package com.example.newslook.news.ui.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newslook.R
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

    private lateinit var dialog: Dialog

    var listThemes: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        dialog = Dialog(this)
        binding.currendDateText.text =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        // Setting up Spinner and adapter
        newsArticleViewModel.prePopulateList()
        listThemes = newsArticleViewModel.getListThemes()
        // Create an ArrayAdapter using a simple spinner layout and languages array
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listThemes)
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.listThemes.adapter = arrayAdapter
        binding.listThemes.adapter.hasStableIds()
        binding.listThemes.setSelection(0, false)
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
        adapter = NewsArticlesAdapter { }
        super.onStop()
    }

    override fun onPause() {
        adapter = NewsArticlesAdapter { }
        super.onPause()
    }

    private fun onItemClick(position: Int) {
        val url = adapter.currentList[position].url
        val newsIntent = Intent(this, WebViewActivity::class.java)
        newsIntent.putExtra("url", url)
        newsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(newsIntent)
    }

    fun updateGUI() {
        val pos = binding.listThemes.selectedItemPosition
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

    private fun showDialog() {
        dialog.setContentView(R.layout.dialog_layout)
        val text = dialog.findViewById<TextView>(R.id.listTextView)
        var list = ""
        listThemes.forEach {
            list += "$it, "
        }
        text.text = list
        val add = dialog.findViewById<Button>(R.id.add)
        add.setOnClickListener {
            showAddDialog()
            dialog.dismiss()
        }
        val edit = dialog.findViewById<Button>(R.id.edit)
        edit.setOnClickListener {
            showEditDialog()
            dialog.dismiss()
        }
        val remove = dialog.findViewById<Button>(R.id.remove)
        remove.setOnClickListener {
            showRemoveDialog()
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    private fun showAddDialog() {
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
            updateGUI()
        })
        builder.setNegativeButton(
            "Отмена",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun showEditDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Изменение категории")
        val context: Context = applicationContext
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val editTarget = EditText(this)
        editTarget.hint = "Введите название изменямой категории"
        editTarget.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(editTarget)

        val editText = EditText(this)
        editText.hint = "Введите название новой категории"
        editText.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(editText)

        builder.setView(layout)
        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            newsArticleViewModel.editListTheme(editTarget.text.toString(), editText.text.toString())
            arrayAdapter.notifyDataSetChanged()
            updateGUI()
        })
        builder.setNegativeButton(
            "Отмена",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun showRemoveDialog() {
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
            updateGUI()
        })
        builder.setNegativeButton(
            "Отмена",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}