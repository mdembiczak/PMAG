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
        PMAGApp.firebaseAuth.signInWithEmailAndPassword(email, password)
        val firebaseUser = PMAGApp.fUser

        if (PMAGApp.fUser != null) {
            return Result.Success(User())
        }
        return Result.Error(IOException("Error logging in"))
    }

    fun logout() {
        PMAGApp.firebaseAuth.signOut()
    }
}

