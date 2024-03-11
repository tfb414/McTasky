package com.example.mctasky

//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.mctasky.data.TaskRepository
//import com.example.mctasky.network.NetworkModule
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory

//class MainActivity : AppCompatActivity() {
//    private val taskRepository = TaskRepository()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.getTasks)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        val getTasksButton: Button = findViewById(R.id.getTasks)
//        getTasksButton.setOnClickListener {
//            fetchTasks()
//        }
//    }
//
//    private fun fetchTasks() {
//        taskRepository.fetchTasks(
//            onSuccess = { tasks ->
//                val taskTextView: TextView = findViewById(R.id.taskTextView)
//                taskTextView.text = tasks.joinToString("\n") { it.task }
//            },
//            onError = { message ->
//                val taskTextView: TextView = findViewById(R.id.taskTextView)
//                taskTextView.text = message
//            }
//        )
//    }
//}

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mctasky.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.migration.CustomInjection.inject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

//        viewModel.tasks.observe(this) { tasks ->
//            val taskTextView: TextView = findViewById(R.id.taskTextView)
//            taskTextView.text = tasks.joinToString("\n") { it.task }
//        }

        viewModel.taskTypes.observe(this) { taskTypes ->
            val taskTypesTextView: TextView = findViewById(R.id.taskTypesTextView)
            taskTypesTextView.text = taskTypes.joinToString("\n") { it?.taskName ?: "Unknown"  } ?: "hey"
        }
//
//        viewModel.errorMessage.observe(this) { message ->
//            val taskTextView: TextView = findViewById(R.id.taskTypesTextView)
//            taskTextView.text = message
//        }
    }
}



