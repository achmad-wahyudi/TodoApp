package com.wahyuapp.todoapp

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wahyuapp.todoapp.database.Todo
import com.wahyuapp.todoapp.databinding.ListItemBinding

class TodoAdapter(private val viewModel: TodoViewModel) :
    ListAdapter<Todo, TodoAdapter.MyViewHolder>(TodoDiffCallback()) {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("testing", "onBindViewHolder: " + getItem(position).task)
        holder.todoText.text = getItem(position).task

        //menghapus
        holder.delBtn.setOnClickListener {
            viewModel.removeTodo(getItem(position))
        }

        //mengedit
        holder.ediBtn.setOnClickListener {
            val context = holder.itemView.context

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.edit_item, null)

            //mengambil data sebelumnya
            val prevText = getItem(position).task
            val editText = view.findViewById<TextView>(R.id.editText)
            editText.text = prevText

            //dialog
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Edit Item")
                .setView(view)
                .setPositiveButton("Update") { dialog, id ->

                    //edit
                    val editedText = editText.text.toString()
                    getItem(position).task = editedText
                    viewModel.updateTodo(getItem(position))
                    holder.todoText.text = editedText

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            alertDialog.create().show()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    var itemCountTmp = if (viewModel.todos.value == null) 0 else viewModel.todos.value!!.size
//    override fun getItemCount() = itemCountTmp

    class MyViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val todoText = binding.todoItem
        val delBtn = binding.btnDelete
        val ediBtn = binding.btnEdit
    }
}

class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(p0: Todo, p1: Todo): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: Todo, p1: Todo): Boolean {
        return p0 == p1
    }
}