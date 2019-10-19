package com.management.pmag.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.management.pmag.model.entity.User

class UserRepository {
    private val usersPath = "users"
    private var usersCollection = FirebaseFirestore.getInstance().collection(usersPath)

    fun getUserDetails(userId: String): User {
        usersCollection.whereEqualTo("userId", userId).get()
            .addOnSuccessListener {} //set user informations
            .addOnFailureListener {} //throw Exception
//            .addOn{ //hrow Exception}

        return User("", "", "", "", "", "")
    }
}