package com.example.newslook.news.ui.activity

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslook.R
import com.example.newslook.core.ui.ViewState
import com.example.newslook.core.utils.observeNotNull
import com.example.newslook.databinding.FragmentNewsBinding
import com.example.newslook.news.storage.CategoryItem
import com.example.newslook.news.ui.adapter.NewsArticlesAdapter
import com.example.newslook.news.ui.viewmodel.NewsArticleViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.row_news_article.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null

    private val newsArticleViewModel: NewsArticleViewModel by activityViewModels()

    private lateinit var adapter: NewsArticlesAdapter

    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var theme: String

    private lateinit var dialog: Dialog

    private var itemCount = 0

    private var listThemes: MutableList<CategoryItem> = mutableListOf()

    var listThemesString: MutableList<String> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        dialog = Dialog(context!!)
        binding.currentDateText.text =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        // Setting up Spinner and adapter
        listThemes = newsArticleViewModel.getListThemes()
        listThemesString = listThemes.map { it.name }.toMutableList()
        // Create an ArrayAdapter using a simple spinner layout and languages array
        arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listThemesString)
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
                theme = listThemesString[position]
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
        binding.newsList.layoutManager = LinearLayoutManager(context!!)
        binding.newsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = getFirstVisiblePosition(recyclerView)
                binding.currentDateText.text = adapter.currentList[position].publishedAt!!.replace(Regex("""[T,Z]""")," ")
            }
        })
        updateGUI()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editThemes.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(position: Int) {
        val url = adapter.currentList[position].url
        newsArticleViewModel.saveUrl(url!!)
        findNavController().navigate(R.id.action_FirstFragment_to_WebViewFragment)
    }

    fun getFirstVisiblePosition(rv: RecyclerView?): Int {
        if (rv != null) {
            val layoutManager = rv
                .layoutManager
            if (layoutManager is LinearLayoutManager) {
                return layoutManager
                    .findFirstVisibleItemPosition()
            }
        }
        return 0
    }

    fun updateGUI() {
        val pos = binding.listThemes.selectedItemPosition
        theme = listThemesString[pos]
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
}