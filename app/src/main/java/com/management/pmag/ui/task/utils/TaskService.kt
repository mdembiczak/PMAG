package com.management.pmag.ui.task.utils

import android.widget.DatePicker
import com.management.pmag.model.repository.TaskRepository

object TaskService {
    private val taskRepository = TaskRepository()

    fun saveTask(
        taskTag: String,
        taskState: String,
        taskAssignedTo: String,
        taskDescription: String,
        dueDate: String
    ) {
        taskRepository.update(taskTag, taskState, taskAssignedTo, taskDescription, dueDate)
    }

    fun dateHandling(datePicker: DatePicker): String {
        return if (datePicker.month < 10) {
            "${datePicker.year}-0${datePicker.month}-${datePicker.dayOfMonth}"
        } else {
            "${datePicker.year}-${datePicker.month}-${datePicker.dayOfMonth}"
        }
    }
}