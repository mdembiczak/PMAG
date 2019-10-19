package com.management.pmag.controller.login

import com.management.pmag.PMAGApp
import com.management.pmag.model.entity.Result
import com.management.pmag.model.entity.User
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(email: String, password: String): Result<User> {
        val signInWithEmailAndPassword =
            PMAGApp.firebaseAuth.signInWithEmailAndPassword(email, password)

        val currentUser = PMAGApp.firebaseAuth.currentUser


        val uid = PMAGApp.firebaseAuth.currentUser?.uid.orEmpty()

        lateinit var user: User

        if (uid.isNotEmpty()) {
            user = User(
                "123",
                "firstName",
                "lastName",
                "address",
                "phoneNumber",
                "email"
            )
            val loggedInUser = User(
                uid,
                user.firstName,
                user.lastName,
                user.addressId,
                user.phoneNumber,
                user.phoneNumber
            )
            return Result.Success(loggedInUser)
        }
        return Result.Error(IOException("Error logging in"))
    }

    fun logout() {
        PMAGApp.firebaseAuth.signOut()
    }
}

