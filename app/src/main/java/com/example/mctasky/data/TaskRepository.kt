package com.example.mctasky.data

import com.example.mctasky.Task
import com.example.mctasky.TaskType
import com.example.mctasky.network.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import android.util.Log

class TaskRepository @Inject constructor(
) {
    @Inject lateinit var taskService: TaskService

    fun fetchTasks(onSuccess: (List<Task>) -> Unit, onError: (String) -> Unit) {
        taskService.getTasks().enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess) ?: onError("Empty Task List")
                } else {
                    onError("Request failed with Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun fetchTaskTypes(onSuccess: (List<TaskType>) -> Unit, onError: (String) -> Unit) {
        taskService.getTaskTypes().enqueue(object : Callback<List<TaskType>> {
            override fun onResponse(call: Call<List<TaskType>>, response: Response<List<TaskType>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess) ?: onError("Empty TaskTypes List")
                } else {
                    onError("Request failed with Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TaskType>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun postTaskType(taskType: TaskType, onSuccess: (TaskType) -> Unit, onError: (String) -> Unit) {
        taskService.postTaskTypes(taskType).enqueue(object : Callback<TaskType> {
            override fun onResponse(call: Call<TaskType>, response: Response<TaskType>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess) ?: onError("Task Type creation successful")
                } else {
                    onError("Request failed with Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TaskType>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }


    fun postTask(task: Task, onSuccess: (Task) -> Unit, onError: (String) -> Unit) { // Adjust the onSuccess return type if needed
        taskService.postTask(task).enqueue(object : Callback<Task> { // Or your relevant API response type
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess) ?: onError("Task creation successful")
                } else {
                    onError("Request failed with Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }
}