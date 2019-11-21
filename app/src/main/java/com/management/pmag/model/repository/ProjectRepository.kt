package com.management.pmag.model.repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.model.entity.Project

class ProjectRepository {
    private val projectPath = "projects"
    private var projectsCollection = FirebaseFirestore.getInstance().collection(projectPath)

    private val projectStatusFieldName = "projectStatus"
    private val projectTagFieldName = "projectTag"
    private val projectOwnerIdFieldName = "projectOwnerId"
    private val projectNameFieldName = "projectName"
    private val projectDescriptionFieldName = "projectDescription"
    private val projectUsersFieldName = "projectUsers"


    fun saveProject(project: Project) {
        projectsCollection
            .whereEqualTo(projectTagFieldName, project.projectTag)
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

    fun getProjectsByOwnerId(emailAddressOwnerId: String): Query {
        return projectsCollection
            .whereEqualTo(projectOwnerIdFieldName, emailAddressOwnerId)
    }

    fun getProjectByProjectTag(projectTag: String): Task<QuerySnapshot> {
        return projectsCollection
            .whereEqualTo(projectTagFieldName, projectTag)
            .get()
    }

    fun getProjectByUserEmail(emailAddress: String): Query {
        return projectsCollection
            .whereArrayContains(
                projectUsersFieldName,
                emailAddress
            )
    }

    fun updateProject(
        projectTag: String, projectName: String, projectState: String, projectDescription: String
    ) {
        getProjectByProjectTag(projectTag)
            .addOnSuccessListener {
                Log.d(TAG, "Project id: $projectTag updated")
                val documentId = it.documents.first().id
                projectsCollection.document(documentId).update(
                    projectNameFieldName, projectName,
                    projectStatusFieldName, projectState,
                    projectDescriptionFieldName, projectDescription
                )
            }
            .addOnFailureListener {
                Log.e(TAG, it.toString())
            }
    }

    fun updateProjectUser(projectTag: String, email: String) {
        getProjectByProjectTag(projectTag)
            .addOnSuccessListener {
                Log.d(TAG, "Project users updated for projectTag: $projectTag")
                val documentId = it.documents.first().id
                projectsCollection
                    .document(documentId)
                    .update(projectUsersFieldName, FieldValue.arrayUnion(email))
            }
            .addOnFailureListener {
                Log.e(TAG, it.toString())
            }
    }
}