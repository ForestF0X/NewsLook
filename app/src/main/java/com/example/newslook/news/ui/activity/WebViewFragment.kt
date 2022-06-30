package com.example.newslook.news.ui.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newslook.R
import com.example.newslook.databinding.FragmentCategoryBinding
import com.example.newslook.databinding.FragmentWebViewBinding
import com.example.newslook.news.storage.CategoryItem
import com.example.newslook.news.ui.adapter.CategoryAdapter
import com.example.newslook.news.ui.viewmodel.NewsArticleViewModel

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null

    private val newsArticleViewModel: NewsArticleViewModel by activityViewModels()

    private lateinit var webView: WebView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val url = newsArticleViewModel.getUrl()
        webView = binding.webView
        webView.webViewClient = WebViewClient()
        // this will load the url of the website
        webView.loadUrl(url)
        // this will enable the javascript settings
        webView.settings.javaScriptEnabled = true
        // this will enable the zoom on webpage
        webView.settings.setSupportZoom(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.backButton.setOnClickListener {
//            findNavController().navigate(R.id.action_ThirdFragment_to_FirstFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}