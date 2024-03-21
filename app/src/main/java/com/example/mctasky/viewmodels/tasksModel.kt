package com.example.mctasky.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mctasky.Task
import com.example.mctasky.TaskTypes
import com.example.mctasky.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _taskTypes = MutableLiveData<List<TaskTypes>>()
    val taskTypes: LiveData<List<TaskTypes>> = _taskTypes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        fetchTaskTypes()
    }

    private fun fetchTasks() {
        taskRepository.fetchTasks(
            onSuccess = { _tasks.value = it },
            onError = { _errorMessage.value = it }
        )
    }

    private fun fetchTaskTypes() {
        taskRepository.fetchTaskTypes(
            onSuccess = { _taskTypes.value = it },
            onError = { _errorMessage.value = it }
        )
    }

    fun postTask(task: Task, onSuccess: (Task) -> Unit, onError: (String) -> Unit) {
        taskRepository.postTask(task, onSuccess, onError)
    }
}