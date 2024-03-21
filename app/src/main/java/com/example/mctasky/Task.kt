package com.example.mctasky

data class Task(
    val task: String,
    val person: String,
    val amount: Number,
    val unit: String?,
)

fun createTask(task: Task): Task {
    return Task(task = task.task, person = "Tim", amount =task.amount, unit =task.unit)
}

fun createTaskFromInput(task: String, amount: Number, unit: String): Task {
    return Task(task = task, person = "Tim", amount =amount, unit =unit)
}

data class TaskTypes(
    val id: String,
    val taskName: String,
    val description: String,
)