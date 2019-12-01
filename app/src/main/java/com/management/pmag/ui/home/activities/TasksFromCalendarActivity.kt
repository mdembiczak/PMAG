package com.management.pmag.ui.home.activities

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.TaskRepository
import com.management.pmag.model.repository.UserRepository
import com.management.pmag.ui.task.utils.TaskAdapter
import kotlinx.android.synthetic.main.activity_tasks_from_calendar.*

class TasksFromCalendarActivity : AppCompatActivity() {

    private lateinit var taskFromCalendar: ListView
    private val taskRepository = TaskRepository()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_from_calendar)
        taskFromCalendar = findViewById(R.id.taskFromCalendarListView)

        val dueDate = intent.getSerializableExtra("DUE_DATE") as String?
        userRepository.getUserQuery(PMAGApp.firebaseAuth.currentUser?.email!!)
            .addSnapshotListener { userQuery, _ ->
                val user = userQuery?.toObjects(User::class.java)?.first()!!
                taskRepository.getQueryTaskByDueDate(user.projectContext, dueDate!!)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        val taskList = queryTaskList(firebaseFirestoreException, querySnapshot)
                        if (taskList.isNotEmpty()) {
                            taskFromCalendarListView.adapter =
                                TaskAdapter(PMAGApp.ctx, ArrayList(taskList))
                        }
                    }

            }
    }

    fun queryTaskList(
        exception: FirebaseFirestoreException?,
        snapshot: QuerySnapshot?
    ): List<Task> {
        if (exception != null) {
            Log.e(ContentValues.TAG, "Listen Failed", exception)
            return emptyList()
        }
        if (snapshot!!.isEmpty) {
            Log.d(ContentValues.TAG, "Task list is empty")
            return emptyList()
        }
        return snapshot.toObjects(Task::class.java)
    }
}
