package com.management.pmag.ui.task.common

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.ui.task.utils.TaskService
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var taskTitle: TextView
    private lateinit var taskState: Spinner
    private lateinit var taskCreatorEmail: TextView
    private lateinit var taskAssignedTo: TextInputLayout
    private lateinit var taskDescription: TextInputLayout
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        init()

        val task = intent.getSerializableExtra("TASK") as Task?
        taskTitle.text = task?.title.orEmpty()
        task?.state?.let { initializeTaskStateSpinner(taskState, it) }
        taskCreatorEmail.text = task?.taskCreatorId.orEmpty()
        taskAssignedTo.editText?.setText(task?.assignedTo.orEmpty())
        taskDescription.editText?.setText(task?.description.orEmpty())
        saveButtonOnClickListener(task?.taskTag!!)
    }


    private fun init() {
        taskTitle = findViewById(R.id.taskTitleTextView)
        taskState = findViewById(R.id.taskStateSpinner)
        taskCreatorEmail = findViewById(R.id.taskCreatorEmailTextView)
        taskAssignedTo = findViewById(R.id.taskAssignedToTextInputLayout)
        taskDescription = findViewById(R.id.taskDescriptionTextInputLayout)
        saveButton = findViewById(R.id.saveTaskButton)
    }

    private fun initializeTaskStateSpinner(taskStateSpinner: Spinner, state: String = "default") {
        ArrayAdapter.createFromResource(
            PMAGApp.ctx,
            R.array.task_state_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            taskStateSpinner.adapter = adapter
            if (state != "default") {
                val position = adapter.getPosition(state)
                taskStateSpinner.setSelection(position)
            }
        }
    }

    private fun saveButtonOnClickListener(taskTag: String) {
        saveTaskButton.setOnClickListener {
            TaskService.saveTask(
                taskTag, taskState.selectedItem.toString(),
                taskAssignedTo.editText?.text.toString(), taskDescription.editText?.text.toString()
            )
        }
    }
}
