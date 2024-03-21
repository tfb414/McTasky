package com.example.mctasky

data class Task(
    val task: String,
    val person: String,
    val amount: Float,
    val unit: String?,
)

fun createTask(task: Task): Task {
    return Task(task = task.task, person = "Tim", amount=task.amount, unit=task.unit)
}

data class TaskTypes(
    val id: String,
    val taskName: String,
    val description: String,
)