package com.management.pmag.ui.authorization.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.management.pmag.PMAGApp
import com.management.pmag.ui.dashboard.DashboardActivity
import com.management.pmag.R

class RegisterActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var signUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.registerEmail)
        password = findViewById(R.id.registerPassword)
        signUp = findViewById(R.id.signUpButton)


        signUp.setOnClickListener {
            val email: String = email.text.toString()
            val password: String = password.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Please fill in the required fields", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Please fill in the required fields", Toast.LENGTH_SHORT).show()
            }
            if (password.length < 8) {
                Toast.makeText(applicationContext, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            }

            PMAGApp.fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(applicationContext, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Email or password is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        if (PMAGApp.fAuth.currentUser != null) {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
        }
    }
}

