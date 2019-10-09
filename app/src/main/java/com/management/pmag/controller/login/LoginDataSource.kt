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
        val successful =
            PMAGApp.firebaseAuth.signInWithEmailAndPassword(email, password).isSuccessful

        //rest request get full user data
        lateinit var user: User // =???? request do firestone

        if (successful) {
            val userId = PMAGApp.firebaseAuth.currentUser?.uid.orEmpty()
            user = User(
                "123",
                "firstName",
                "lastName",
                "address",
                "phoneNumber",
                "email"
            )
            val loggedInUser = User(
                userId,
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
        // TODO: revoke authentication
    }
}

