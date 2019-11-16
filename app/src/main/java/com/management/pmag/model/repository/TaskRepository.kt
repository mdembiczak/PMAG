package com.management.pmag.model.repository

import com.google.firebase.firestore.FirebaseFirestore

class TaskRepository {
    private val tasksPath = "users"
    private var tasksCollection = FirebaseFirestore.getInstance().collection(tasksPath)

    fun getTaskByTaskTag() {
//        return tasksCollection
//            .whereEqualTo()
    }
}