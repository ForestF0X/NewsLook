package com.example.newslook.news.ui.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newslook.databinding.FragmentCategoryBinding
import com.example.newslook.news.storage.CategoryItem
import com.example.newslook.news.ui.adapter.CategoryAdapter
import com.example.newslook.news.ui.viewmodel.NewsArticleViewModel
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null

    private val newsArticleViewModel: NewsArticleViewModel by activityViewModels()

    private lateinit var dialog: Dialog

    private lateinit var adapter: CategoryAdapter

    var listThemes: MutableList<CategoryItem> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        dialog = Dialog(context!!)
        listThemes = newsArticleViewModel.getListThemes()
        binding.floatingActionButton.setOnClickListener {
            showAddDialog()
        }
        binding.categoryList
        adapter = CategoryAdapter(context!!, listThemes)
        binding.categoryList.adapter = adapter
        binding.categoryList.layoutManager = LinearLayoutManager(context!!)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedCourse: CategoryItem =
                    listThemes[viewHolder.absoluteAdapterPosition]
                showDeleteDialog(deletedCourse.name)
                adapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
            }
        }).attachToRecyclerView(binding.categoryList)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val editedCourse: CategoryItem =
                    listThemes[viewHolder.absoluteAdapterPosition]
                showEditDialog(editedCourse.name)
                adapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
            }
        }).attachToRecyclerView(binding.categoryList)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDeleteDialog(categoryDelete: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.setTitle("Удаление категории")

        // Set up the text view
        val text = TextView(context!!)
        text.text = "Вы уверены что хотите удалить эту категорию?"
        builder.setView(text)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            newsArticleViewModel.removeListThemes(categoryDelete)
            adapter.notifyDataSetChanged()
        })
        builder.setNegativeButton(
            "Отмена",
            DialogInterface.OnClickListener { dialog, which ->
                adapter.notifyDataSetChanged()
                dialog.cancel() })
        builder.show()
    }

    private fun showAddDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.setTitle("Добавление категории")

        // Set up the input
        val input = EditText(context!!)
        input.hint = "Введите название новой категории"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            newsArticleViewModel.addListThemes(input.text.toString())
            adapter.notifyDataSetChanged()
        })
        builder.setNegativeButton(
            "Отмена",
            DialogInterface.OnClickListener { dialog, which ->
                adapter.notifyDataSetChanged()
                dialog.cancel() })
        builder.show()
    }

    private fun showEditDialog(categoryEdit: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Изменение категории")
        val context: Context = requireActivity()
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val editText = EditText(context)
        editText.hint = "Введите название новой категории"
        editText.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(editText)

        builder.setView(layout)
        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            newsArticleViewModel.editListTheme(categoryEdit, editText.text.toString())
            adapter.notifyDataSetChanged()
        })
        builder.setNegativeButton(
            "Отмена",
            DialogInterface.OnClickListener { dialog, which ->
                adapter.notifyDataSetChanged()
                dialog.cancel() })

        builder.show()
    }
}