package com.example.mctasky

data class Task(
    val id: String,
    val task: String,
    val person: String,
)

data class TaskTypes(
    val id: String,
    val taskName: String,
    val description: String,
)