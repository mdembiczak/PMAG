package com.management.pmag

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Singletons
 */
class PMAGApp : Application() {

    override fun onCreate() {
        super.onCreate()
        res = resources
        ctx = applicationContext

        firebaseAuth = FirebaseAuth.getInstance()
        fUser = firebaseAuth.currentUser
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var ctx: Context
        lateinit var res: Resources

        lateinit var firebaseAuth: FirebaseAuth

        var fUser: FirebaseUser? = null
    }
}