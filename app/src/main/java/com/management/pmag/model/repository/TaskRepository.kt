package com.management.pmag.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TaskRepository {
    private val tasksPath = "users"
    private var tasksCollection = FirebaseFirestore.getInstance().collection(tasksPath)

    private val assignedToFieldName = "assignedTo"

    fun getTaskByTaskTag() {
//        return tasksCollection
//            .whereEqualTo()
    }

    fun getTaskByAssigned(emailAddress: String): Query {
        return tasksCollection
            .whereEqualTo(assignedToFieldName, emailAddress)
    }
}