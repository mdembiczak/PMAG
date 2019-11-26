package com.management.pmag.ui.task.common

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.util.Strings
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.TaskRepository
import com.management.pmag.model.repository.UserRepository
import java.time.LocalDateTime

class TaskCreationActivity : AppCompatActivity() {

    private lateinit var taskTitle: TextInputLayout
    private lateinit var taskDescription: TextInputLayout
    private lateinit var saveTaskFloatingButton: FloatingActionButton

    private val taskRepository = TaskRepository()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_creation)

        init()
        saveTaskOnClickListener()
    }

    private fun init() {
        taskTitle = findViewById(R.id.taskTitleTextInputLayout)
        taskDescription = findViewById(R.id.taskDescriptionTextInputLayout)
        saveTaskFloatingButton = findViewById(R.id.saveTaskFloatingButton)
    }

    private fun saveTaskOnClickListener() {
        saveTaskFloatingButton.setOnClickListener {
            val userQuery = userRepository.getUser(PMAGApp.firebaseAuth.currentUser?.email!!)
            userQuery.addOnSuccessListener {
                Log.d(ContentValues.TAG, "User query finished success")
                val user = it.toObjects(User::class.java).first()
                taskRepository.getTasksByProjectTag(user.projectContext)
                    .addOnSuccessListener { snapshot ->
                        val taskList = snapshot.toObjects(Task::class.java)
                        val nextTaskNumber = taskList.count() + 1
                        Log.d(ContentValues.TAG, "Next task number is $nextTaskNumber")
                        if (areFiledFields()) {
                            val task = buildTask(nextTaskNumber, user.projectContext)
                            taskRepository.save(task)
                        }
                    }
            }
        }
    }

    private fun areFiledFields(): Boolean {
        if (Strings.isEmptyOrWhitespace(taskTitle.editText?.text.toString())) {
            Toast.makeText(applicationContext, "Task title is not filled", Toast.LENGTH_LONG).show()
            return false
        } else if (Strings.isEmptyOrWhitespace(taskDescription.editText?.text.toString())) {
            Toast.makeText(applicationContext, "Task description is not filled", Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    private fun buildTask(nextTaskNumber: Int, projectContext: String): Task {
        val taskTag = buildTaskTag(nextTaskNumber, projectContext)
        val dateNow = LocalDateTime.now()
        val formattedDate = "${dateNow.dayOfMonth} ${dateNow.month} ${dateNow.year}"
        return Task(
            taskTag,
            projectContext,
            taskTitle.editText?.text.toString(),
            taskDescription.editText?.text.toString(),
            TASK_INITIALIZE_STATUS,
            PMAGApp.firebaseAuth.currentUser?.email!!,
            UNASSIGNED,
            formattedDate
        )
    }

    private fun buildTaskTag(nextTaskNumber: Int, projectContext: String): String {
        return "${projectContext}-${nextTaskNumber}"
    }

    companion object {
        private const val TASK_INITIALIZE_STATUS = "Open"
        private const val UNASSIGNED = "Unassigned"
    }
}
