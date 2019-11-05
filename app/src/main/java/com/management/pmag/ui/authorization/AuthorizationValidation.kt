package com.management.pmag.ui.authorization

import android.util.Patterns

class AuthorizationValidation {
    fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }
}