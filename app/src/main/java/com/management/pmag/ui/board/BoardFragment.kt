package com.management.pmag.ui.board

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.util.Strings
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.TaskRepository
import com.management.pmag.model.repository.UserRepository
import com.management.pmag.ui.task.TaskFragment
import com.management.pmag.ui.task.common.TaskDetailsActivity
import com.management.pmag.ui.task.utils.TaskAdapter

class BoardFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var boardViewModel: BoardViewModel
    private lateinit var taskStatusSpinner: Spinner
    private lateinit var tasksBoardListView: ListView

    private val taskRepository = TaskRepository()
    private val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        boardViewModel =
            ViewModelProviders.of(this).get(BoardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_board, container, false)
        val textView: TextView = root.findViewById(R.id.text_board)
        boardViewModel.text.observe(this, Observer {
            textView.text = it
        })

        init(root)
        initializeTaskStateSpinner(taskStatusSpinner)
        taskStatusSpinner.onItemSelectedListener = this
        onClickTaskItemList(tasksBoardListView)

        return root
    }

    private fun init(root: View) {
        taskStatusSpinner = root.findViewById(R.id.tasksOnBoardStatusSpinner)
        tasksBoardListView = root.findViewById(R.id.taskBoardListView)
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val status = parent?.getItemAtPosition(position).toString()
        loadTasksToTaskListView(tasksBoardListView, status)
        Log.d(TAG, status)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(TAG, "Nothing selected on spinner")
    }

    private fun loadTasksToTaskListView(taskListView: ListView, status: String) {
        userRepository.getUserQuery(PMAGApp.firebaseAuth.currentUser?.email!!)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObjects(User::class.java)?.first()
                if (!Strings.isEmptyOrWhitespace(user?.projectContext)) {
                    taskRepository.getQueryTaskByProjectTagAndStatus(user?.projectContext!!, status)
                        .addSnapshotListener { taskSnapshot, taskException ->
                            val validTaskList = queryTaskList(taskException, taskSnapshot)
                            if (validTaskList.isNotEmpty()) {
                                Log.d(
                                    TAG,
                                    "The are some existing tasks in current project context"
                                )
                                taskListView.adapter =
                                    TaskAdapter(PMAGApp.ctx, ArrayList(validTaskList))
                            } else {
                                taskListView.adapter =
                                    TaskAdapter(PMAGApp.ctx, ArrayList(emptyList()))
                            }
                        }
                }
            }
    }

    private fun queryTaskList(
        exception: FirebaseFirestoreException?,
        snapshot: QuerySnapshot?
    ): List<Task> {
        if (exception != null) {
            Log.e(TAG, "Listen Failed", exception)
            return emptyList()
        }
        if (snapshot!!.isEmpty) {
            Log.d(TAG, "Task list is empty")
            return emptyList()
        }
        return snapshot.toObjects(Task::class.java)
    }

    private fun onClickTaskItemList(listView: ListView) {
        listView.setOnItemClickListener { _, view, _, _ ->
            val taskTagTextView = view.findViewById(R.id.taskTagTextView) as TextView
            val taskTag = taskTagTextView.text.toString()
            taskRepository.getTaskByTaskTag(taskTag)
                .addOnSuccessListener {
                    val task = it.toObjects(Task::class.java).first()
                    val taskDetailsIntent = Intent(context, TaskDetailsActivity::class.java)
                    taskDetailsIntent.putExtra(TaskFragment.taskExtra, task)
                    startActivity(taskDetailsIntent)
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failure on click task item.", it)
                }
        }
    }
}