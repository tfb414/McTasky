package com.example.mctasky

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.getTasks)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getTasksButton: Button = findViewById(R.id.getTasks)
        getTasksButton.setOnClickListener {
            fetchTasks() // Function to handle the network request
        }
    }

    private fun fetchTasks() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.35:3000/") // Your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val taskService = retrofit.create(TaskService::class.java)
        val taskCall = taskService.getTasks() // Assuming this endpoint exists

        taskCall.enqueue(object : Callback<List<Task>> { // Assuming tasks return as a List
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    val tasks = response.body()
                    if (tasks != null) {
                        val taskTextView: TextView = findViewById(R.id.taskTextView)
                        taskTextView.text = tasks.joinToString("\n") { it.task } // Simple formatting
                    }
                } else {
                    displayError("Request failed with Error Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                displayError("Network Error: ${t.message}")
            }

            // Helper function to simplify error display
            private fun displayError(message: String) {
                val taskTextView: TextView = findViewById(R.id.taskTextView)
                taskTextView.text = message
            }
        })
    }
}

