package com.example.mctasky

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



