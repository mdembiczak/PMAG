package com.management.pmag.model.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.model.entity.User

class UserRepository {
    private val projectContextFieldName = "projectContextFieldName"
    private val firstNameFieldName = "firstName"
    private val lastNameFieldName = "lastName"
    private val cityFieldName = "city"
    private val phoneNumberFieldName = "phoneNumber"
    private val usersPath = "users"
    private var usersCollection = FirebaseFirestore.getInstance().collection(usersPath)


    fun getUserQuery(emailAddress: String?): Query {
        return usersCollection.whereEqualTo("emailAddress", emailAddress)
    }

    fun getUser(emailAddress: String?): Task<QuerySnapshot> {
        return getUserQuery(emailAddress).get()
    }

    fun saveUser(user: User) {
        usersCollection.add(user)
            .addOnSuccessListener { Log.d(TAG, "User was added successfully") }
            .addOnFailureListener { Log.e(TAG, "Error with saving user $it") }
    }

    fun updateProjectContext(emailAddress: String?, projectTag: String) {
        getUser(emailAddress)
            .addOnSuccessListener {
                val documentId = it.documents.first().id
                usersCollection.document(documentId).update(
                    projectContextFieldName, projectTag
                )
                Log.d(TAG, "Project context for user $emailAddress updated")
            }
    }

    fun updateUserDetails(
        emailAddress: String?,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        city: String
    ) {
        getUser(emailAddress)
            .addOnSuccessListener {
                Log.d(TAG, "User details updated for user: $emailAddress")
                val documentId = it.documents.first().id
                usersCollection.document(documentId).update(
                    firstNameFieldName, firstName,
                    lastNameFieldName, lastName,
                    phoneNumberFieldName, phoneNumber,
                    cityFieldName, city
                )
            }
            .addOnFailureListener {
                Log.e(TAG, "ERROR: User details update failed")
            }
    }
}