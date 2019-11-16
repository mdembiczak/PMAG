package com.management.pmag.controller.login

import android.content.ContentValues.TAG
import android.util.Log
import com.management.pmag.PMAGApp

class LoginDataSource {
    fun logout() {
        PMAGApp.firebaseAuth.signOut()
        Log.d(TAG, "User logout")
    }
}

