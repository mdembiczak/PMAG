package com.management.pmag.model.repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.model.entity.Project

class ProjectRepository {
    private val projectPath = "projects"
    private var projectsCollection = FirebaseFirestore.getInstance().collection(projectPath)

    fun saveProject(project: Project) {
        projectsCollection
            .whereEqualTo("projectTag", project.projectTag)
            .get()
            .addOnSuccessListener { result ->
                val projectList = result.toObjects(Project::class.java)
                if (projectList.isNotEmpty()) {
                    projectList.first()
                        .let { data -> Log.i(TAG, "Project with name ${data.projectName} exist") }
                    Toast.makeText(
                        PMAGApp.ctx,
                        "Project with projectTag: '${project.projectTag}' exist.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    save(project)
                    Toast.makeText(
                        PMAGApp.ctx,
                        "Project was added successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun save(project: Project) {
        projectsCollection.add(project)
            .addOnSuccessListener { Log.d(TAG, "Project was added successfully") }
            .addOnFailureListener { Log.e(TAG, "Error with saving project $it") }
    }

    fun getProjectsByOwnerId(): Query {
        return projectsCollection
            .whereEqualTo("projectOwnerId", PMAGApp.firebaseAuth.currentUser?.uid)
    }

    fun getProjectByProjectTag(projectTag: String): Task<QuerySnapshot> {
        return projectsCollection
            .whereEqualTo("projectTag", projectTag)
            .get()
    }


}