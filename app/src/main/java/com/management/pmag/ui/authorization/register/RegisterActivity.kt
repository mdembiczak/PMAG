package com.management.pmag.ui.authorization.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.common.base.Strings
import com.management.pmag.MainActivity
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.UserRepository

class RegisterActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var signUp: Button
    var requiredFieldsValidation: Boolean = true


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
            when {
                Strings.isNullOrEmpty(email) -> {
                    toastFillRequiredFields()
                    requiredFieldsValidation = false
                }
                Strings.isNullOrEmpty(password) -> {
                    toastFillRequiredFields()
                    requiredFieldsValidation = false
                }
                password.length < 8 -> {
                    toastPasswordIsToShort()
                    requiredFieldsValidation = false
                }
            }
            firebaseCreateUserWithEmailAndPassword(email, password)
        }
    }

    private fun firebaseCreateUserWithEmailAndPassword(email: String, password: String) {
        if (requiredFieldsValidation) {
            PMAGApp.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        userRepository.saveUser(User(emailAddress = email))
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                }
        }
    }

    private fun toastPasswordIsToShort() {
        Toast.makeText(
            applicationContext,
            "Password must be at least 8 characters",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun toastFillRequiredFields() {
        Toast.makeText(
            applicationContext,
            "Please fill required fields",
            Toast.LENGTH_SHORT
        ).show()
    }
}

