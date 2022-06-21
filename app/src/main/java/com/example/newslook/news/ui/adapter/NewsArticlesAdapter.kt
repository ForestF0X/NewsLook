package com.example.newslook.news.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.newslook.R
import com.example.newslook.core.utils.inflate
import com.example.newslook.databinding.RowNewsArticleBinding
import com.example.newslook.news.storage.entity.NewsArticleDb

class NewsArticlesAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<NewsArticleDb, NewsArticlesAdapter.NewsHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsHolder(parent.inflate(R.layout.row_news_article))

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(newsHolder: NewsHolder, position: Int) = newsHolder.bind(getItem(position), onItemClick)

    class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = RowNewsArticleBinding.bind(itemView)

        fun bind(newsArticle: NewsArticleDb, onItemClick: (position: Int) -> Unit) = with(itemView) {
            binding.newsTitle.text = newsArticle.title
            binding.newsAuthor.text = newsArticle.author
            binding.newsPublishedAt.text = newsArticle.publishedAt
            binding.newsImage.load(newsArticle.urlToImage) {
                placeholder(R.drawable.tools_placeholder)
                error(R.drawable.tools_placeholder)
            }
            itemView.setOnClickListener { _ ->
                onItemClick(absoluteAdapterPosition)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsArticleDb>() {
            override fun areItemsTheSame(oldItem: NewsArticleDb, newItem: NewsArticleDb): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: NewsArticleDb, newItem: NewsArticleDb): Boolean = oldItem == newItem
        }
    }
}