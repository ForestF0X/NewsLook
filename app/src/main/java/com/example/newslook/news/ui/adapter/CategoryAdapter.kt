package com.example.newslook.news.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newslook.databinding.RowCategoryBinding
import com.example.newslook.news.storage.CategoryItem

class CategoryAdapter(
    private val context: Context,
    private val categoryList: MutableList<CategoryItem>
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding = RowCategoryBinding.inflate(LayoutInflater.from(context),parent,false)
        return CategoryHolder(binding)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(categoryHolder: CategoryHolder, position: Int) {
        val categoryItem = categoryList[position]
        categoryHolder.bind(categoryItem)
    }

    class CategoryHolder(categoryItemLayoutBinding: RowCategoryBinding)
        : RecyclerView.ViewHolder(categoryItemLayoutBinding.root) {

        private val binding = RowCategoryBinding.bind(itemView)

        fun bind(category: CategoryItem) {
            binding.categoryName.text = category.name
        }
    }
}