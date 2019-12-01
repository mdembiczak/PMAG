package com.management.pmag.ui.task

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.util.Strings
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.TaskRepository
import com.management.pmag.model.repository.UserRepository
import com.management.pmag.ui.task.common.TaskCreationActivity
import com.management.pmag.ui.task.common.TaskDetailsActivity
import com.management.pmag.ui.task.utils.TaskAdapter

class TaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskListView: ListView
    private lateinit var addTaskFloatingButton: FloatingActionButton

    private val taskRepository = TaskRepository()
    private val userRepository = UserRepository()

    companion object {
        const val taskExtra = "TASK"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskViewModel =
            ViewModelProviders.of(this).get(TaskViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task, container, false)
        val textView: TextView = root.findViewById(R.id.text_task)
        taskViewModel.text.observe(this, Observer {
            textView.text = it
        })
        init(root)
        addTaskFloatingButton.setOnClickListener {
            startActivity(Intent(context, TaskCreationActivity::class.java))
        }
        onClickTaskItemList(taskListView)
        loadTasksToTaskListView(taskListView)

        return root
    }

    private fun init(root: View) {
        addTaskFloatingButton = root.findViewById(R.id.addTaskFloatingButton)
        taskListView = root.findViewById(R.id.taskListView)
    }

    private fun loadTasksToTaskListView(taskListView: ListView) {
        userRepository.getUserQuery(PMAGApp.firebaseAuth.currentUser?.email!!)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObjects(User::class.java)?.first()
                if (!Strings.isEmptyOrWhitespace(user?.projectContext)) {
                    taskRepository.getQueryTaskByAssignedTo(
                        PMAGApp.firebaseAuth.currentUser?.email!!,
                        user?.projectContext!!
                    )
                        .addSnapshotListener { taskSnapshot, taskException ->
                            val validTaskList = queryTaskList(taskException, taskSnapshot)
                            if (validTaskList.isNotEmpty()) {
                                Log.d(
                                    ContentValues.TAG,
                                    "The are some existing tasks for user in current project context"
                                )
                                taskListView.adapter =
                                    TaskAdapter(PMAGApp.ctx, ArrayList(validTaskList))
                            }
                        }
                }
            }
    }

    private fun onClickTaskItemList(listView: ListView) {
        listView.setOnItemClickListener { _, view, _, _ ->
            val taskTagTextView = view.findViewById(R.id.taskTagTextView) as TextView
            val taskTag = taskTagTextView.text.toString()
            taskRepository.getTaskByTaskTag(taskTag)
                .addOnSuccessListener {
                    val task = it.toObjects(Task::class.java).first()
                    val taskDetailsIntent = Intent(context, TaskDetailsActivity::class.java)
                    taskDetailsIntent.putExtra(taskExtra, task)
                    startActivity(taskDetailsIntent)
                }
                .addOnFailureListener {
                    Log.e(ContentValues.TAG, "Failure on click task item.", it)
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
}