package com.management.pmag

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

/**
 * Singletons
 */
class PMAGApp : Application() {

    override fun onCreate() {
        super.onCreate()
        res = resources
        ctx = applicationContext

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        fUser = fAuth.currentUser
    }

    companion object {
        lateinit var ctx: Context
        lateinit var res: Resources

        lateinit var fAuth: FirebaseAuth
        lateinit var fDatabase: FirebaseDatabase

        var fUser: FirebaseUser? = null
    }
}