package com.management.pmag.data

import com.management.pmag.PMAGApp
import com.management.pmag.data.model.api.UserModel
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves userModel information.
 */
class LoginDataSource {

    fun login(email: String, password: String): Result<UserModel> {
        val successful = PMAGApp.fAuth.signInWithEmailAndPassword(email, password).isSuccessful

        //rest request get full user data
        lateinit var user: UserModel // =???? request do api

        if (successful) {
            val userId = PMAGApp.fAuth.currentUser?.uid.orEmpty()
            user = UserModel("123", "firstName", "lastName", "address", "phoneNumber", "email")
            val loggedInUser = UserModel(
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

