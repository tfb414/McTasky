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

fun createTaskTypeBody(taskType: String, description: String? = null): TaskType {
    return TaskType(taskName = taskType, description = description)
}


data class TaskType(
    val taskName: String,
    val description: String?
)