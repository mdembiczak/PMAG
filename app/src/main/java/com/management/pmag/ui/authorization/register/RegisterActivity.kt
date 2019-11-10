package com.management.pmag.ui.authorization.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.management.pmag.MainActivity
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.UserRepository

class RegisterActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var signUp: Button

    val userRepository = UserRepository()

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

            PMAGApp.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userRepository.saveUser(
                            User(
                                emailAddress = email,
                                userId = PMAGApp.fUser?.uid.toString()
                            )
                        )
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Email or password is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        if (PMAGApp.firebaseAuth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}

