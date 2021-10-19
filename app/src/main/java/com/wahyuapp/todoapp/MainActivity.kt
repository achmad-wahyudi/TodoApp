package com.wahyuapp.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wahyuapp.todoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: TodoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        viewManager = LinearLayoutManager(this)
        viewAdapter = TodoAdapter(viewModel)

        recyclerView = binding.myRecyclerView

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.btnNew.setOnClickListener {
            if (binding.newText.text.toString().isNotEmpty()) {
                viewModel.addTodo(binding.newText.text.toString())
                binding.newText.text = null
            }
        }

        viewModel.todos.observe(this, { list ->
            viewAdapter.submitList(list.toMutableList())
        })

    }
}
