package com.management.pmag.model.repository

import android.content.ContentValues.*
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.management.pmag.infrastructure.boundary.ObjectsMapper.convertObjectToJson
import com.management.pmag.model.entity.Project

class ProjectRepository {

    private val projectCollection = "projects"

    var projectsCollection = FirebaseFirestore.getInstance()
        .collection(projectCollection)

    fun save(project: Project): String {
        var projectNotExist = true
        projectsCollection.whereArrayContains("projectTag", project.projectTag).get()
            .addOnSuccessListener { projects -> projectNotExist = !projects.isEmpty }
            .addOnFailureListener { Log.e(TAG, "Error with query projects") }

        if(projectNotExist){
            val projectJson = convertObjectToJson(project)
            projectsCollection.document(project.projectTag)
                .set(projectJson)
                .addOnSuccessListener { Log.d(TAG, "Project was added sucessfully: $it") }
                .addOnFailureListener { Log.e(TAG, "Error with saving project $it") }
            return "Project was successfuly created"
        }
        return "ERROR: Project with the same Project Tag exist"
    }
}