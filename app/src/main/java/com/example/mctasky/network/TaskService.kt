package com.example.mctasky.network
import com.example.mctasky.Task
import com.example.mctasky.TaskTypes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaskService {
    @GET("/api/tasks") // The path of your endpoint
    fun getTasks(): Call<List<Task>>

    @GET("/api/taskTypes")
    fun getTaskTypes(): Call<List<TaskTypes>>

    @POST("/api/tasks")
    fun postTask(@Body task: Task): Call<Task>
}