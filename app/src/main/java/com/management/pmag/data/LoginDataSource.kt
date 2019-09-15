package com.management.pmag.data

import com.management.pmag.PMAGApp
import com.management.pmag.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(email: String, password: String): Result<LoggedInUser> {
        val successful = PMAGApp.fAuth.signInWithEmailAndPassword(email, password).isSuccessful
        if (successful) {
            val displayName = PMAGApp.fAuth.currentUser?.displayName.orEmpty()
            val loggedInUser = LoggedInUser(java.util.UUID.randomUUID().toString(), displayName)
            return Result.Success(loggedInUser)
        }
        return Result.Error(IOException("Error logging in"))
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

