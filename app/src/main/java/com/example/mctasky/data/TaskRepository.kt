package com.example.mctasky.data

import com.example.mctasky.Task
import com.example.mctasky.TaskTypes
import com.example.mctasky.network.NetworkModule
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

    fun fetchTaskTypes(onSuccess: (List<TaskTypes>) -> Unit, onError: (String) -> Unit) {
        Log.d("derp", "trying to fix this")
        taskService.getTaskTypes().enqueue(object : Callback<List<TaskTypes>> {
            override fun onResponse(call: Call<List<TaskTypes>>, response: Response<List<TaskTypes>>) {
                if (response.isSuccessful) {
                    Log.d("DERPY3", "updated")
                    Log.d("DERPY2", response.body().toString())
                    response.body()?.let(onSuccess) ?: onError("Empty TaskTypes List")
                } else {
                    onError("Request failed with Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TaskTypes>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }
}