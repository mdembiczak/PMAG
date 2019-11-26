package com.management.pmag.ui.task.utils

import com.management.pmag.model.repository.TaskRepository

object TaskService {
    private val taskRepository = TaskRepository()

    fun saveTask(
        taskTag: String,
        taskState: String,
        taskAssignedTo: String,
        taskDescription: String
    ) {
        taskRepository.update(taskTag, taskState, taskAssignedTo, taskDescription)
    }
}