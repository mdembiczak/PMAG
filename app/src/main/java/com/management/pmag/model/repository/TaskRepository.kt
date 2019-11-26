package com.management.pmag.model.repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.model.entity.Task

class TaskRepository {
    private val tasksPath = "tasks"
    private var taskCollection = FirebaseFirestore.getInstance().collection(tasksPath)

    private val assignedToFieldName = "assignedTo"
    private val taskTagFieldName = "taskTag"
    private val projectTagFieldName = "projectTag"
    private val stateFieldName = "status"
    private val descriptionFieldName = "description"


    fun getTaskByTaskTag(taskTag: String): com.google.android.gms.tasks.Task<QuerySnapshot> {
        return taskCollection
            .whereEqualTo(taskTagFieldName, taskTag)
            .get()
    }

    fun getQueryTaskByProjectTag(defaultProjectTag: String): Query {
        return taskCollection
            .whereEqualTo(projectTagFieldName, defaultProjectTag)
    }

    fun getTasksByProjectTag(defaultProjectTag: String): com.google.android.gms.tasks.Task<QuerySnapshot> {
        return getQueryTaskByProjectTag(defaultProjectTag)
            .get()
    }

    fun save(task: Task) {
        taskCollection.add(task)
            .addOnSuccessListener { Log.d(TAG, "Task was added successfully") }
            .addOnFailureListener { Log.e(TAG, "Error with saving project $it") }
    }

    fun update(
        taskTag: String,
        taskState: String,
        taskAssignedTo: String,
        taskDescription: String
    ) {
        getTaskByTaskTag(taskTag)
            .addOnSuccessListener {
                Log.d(TAG, "Task id: $taskTag updated")
                Toast.makeText(PMAGApp.ctx, "Update task success", Toast.LENGTH_LONG)
                    .show()
                val documentId = it.documents.first().id
                taskCollection.document(documentId).update(
                    taskTagFieldName, taskTag,
                    stateFieldName, taskState,
                    assignedToFieldName, taskAssignedTo,
                    descriptionFieldName, taskDescription
                )
            }
            .addOnFailureListener {
                Log.e(TAG, it.toString())
                Toast.makeText(PMAGApp.ctx, "Update task failed, try again.", Toast.LENGTH_LONG)
                    .show()
            }
    }
}