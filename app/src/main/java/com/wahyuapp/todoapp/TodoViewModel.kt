package com.wahyuapp.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.wahyuapp.todoapp.database.Todo
import com.wahyuapp.todoapp.database.TodoDAO
import com.wahyuapp.todoapp.database.TodoDatabase
import com.wahyuapp.todoapp.database.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    //add repository
    private val repository: TodoRepository
    private val todoDAO: TodoDAO = TodoDatabase.getInstance(application).todoDAO()

    private var _todos: LiveData<List<Todo>>

    val todos: LiveData<List<Todo>>
        get() = _todos

    //coroutine
    private var vmJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + vmJob)

    init {
        repository = TodoRepository(todoDAO)
        _todos = repository.allTodos
    }

    fun addTodo(text: String) {
        uiScope.launch {
            repository.insert(Todo(0, text))
        }
    }

    fun removeTodo(todo: Todo) {
        uiScope.launch {
            repository.delete(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        uiScope.launch {
            repository.update(todo)
        }
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}