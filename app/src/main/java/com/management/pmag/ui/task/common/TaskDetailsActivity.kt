package com.management.pmag.ui.task.common

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.ui.task.utils.TaskService
import kotlinx.android.synthetic.main.activity_task_details.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var taskTitle: TextView
    private lateinit var taskState: Spinner
    private lateinit var taskCreatorEmail: TextView
    private lateinit var taskAssignedTo: TextInputLayout
    private lateinit var taskDescription: TextInputLayout
    private lateinit var saveButton: Button
    private lateinit var datePicker: DatePicker
    private lateinit var dueDate: String


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
        task?.dueDate.let {
            val date = LocalDate.parse(it, DateTimeFormatter.ISO_DATE)
            datePicker.updateDate(date.year, date.monthValue, date.dayOfMonth)
            dueDate = it.orEmpty()
        }
        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            dueDate = if (datePicker.month < 10) {
                "${year}-0${monthOfYear}-${dayOfMonth}"
            } else {
                "${year}-${monthOfYear}-${dayOfMonth}"
            }
            Toast.makeText(PMAGApp.ctx, dueDate, Toast.LENGTH_LONG).show()
        }
        saveButtonOnClickListener(task?.taskTag!!)
    }


    private fun init() {
        taskTitle = findViewById(R.id.taskTitleTextView)
        taskState = findViewById(R.id.taskStatusSpinner)
        taskCreatorEmail = findViewById(R.id.taskCreatorEmailTextView)
        taskAssignedTo = findViewById(R.id.taskAssignedToTextInputLayout)
        taskDescription = findViewById(R.id.taskDescriptionTextInputLayout)
        saveButton = findViewById(R.id.saveTaskButton)
        datePicker = findViewById(R.id.datePickerDetailsActivity)
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
            //sprobowac wrzucic tutaj logike parsowania
            TaskService.saveTask(
                taskTag,
                taskState.selectedItem.toString(),
                taskAssignedTo.editText?.text.toString(),
                taskDescription.editText?.text.toString(),
                dueDate
            )
        }
    }
}
