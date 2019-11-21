package com.management.pmag.ui.task

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.model.repository.TaskRepository
import com.management.pmag.ui.task.utils.TaskAdapter
import java.time.LocalDateTime

class TaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskListView: ListView
    private lateinit var taskAdapter: ArrayAdapter<Task>

    private val taskRepository = TaskRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskViewModel =
            ViewModelProviders.of(this).get(TaskViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        taskViewModel.text.observe(this, Observer {
            textView.text = it
        })

        taskListView = root.findViewById(R.id.taskListView)
        loadTasksToTaskListView(taskListView)

        return root
    }

    private fun loadTasksToTaskListView(taskListView: ListView) {
        taskRepository.getTaskByAssigned(PMAGApp.firebaseAuth.currentUser?.email!!)
            .addSnapshotListener { snapshot, exception ->
                val validTaskList = queryTaskList(exception, snapshot)
                val taskList = buildData()
                if (taskList.isNotEmpty()) {
                    Log.d(ContentValues.TAG, "User has assigned tasks")
                    taskListView.adapter = TaskAdapter(PMAGApp.ctx, taskList)
                }
            }
    }

    private fun queryTaskList(
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

    private fun buildData(): ArrayList<Task> {
        val taskList = ArrayList<Task>()
        taskList.add(putData("TaskTag1", "TaskName1"))
        taskList.add(putData("TaskTag2", "TaskName2"))
        taskList.add(putData("TaskTag3", "TaskName3"))
        return taskList
    }

    private fun putData(s: String, s1: String): Task {
        val dateNow = LocalDateTime.now()
        val formattedDate = "${dateNow.dayOfMonth} ${dateNow.month} ${dateNow.year}"
        return Task(taskTag = s, title = s1, creationData = formattedDate)
    }
}