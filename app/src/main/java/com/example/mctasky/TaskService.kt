package com.example.mctasky
import retrofit2.Call
import retrofit2.http.GET

interface TaskService {
    @GET("/api/tasks") // The path of your endpoint
    fun getTasks(): Call<List<Task>>
}