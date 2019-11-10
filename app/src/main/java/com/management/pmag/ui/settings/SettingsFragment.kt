package com.management.pmag.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.UserRepository

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var firstNameInputLayout: TextInputLayout
    private lateinit var lastNameInputLayout: TextInputLayout
    private lateinit var cityInputLayout: TextInputLayout
    private lateinit var phoneNumberInputLayout: TextInputLayout
    private lateinit var emailAddressTextView: TextView
    private lateinit var saveUserDetailsButton: Button

    val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_settings)
        settingsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        getFields(root)
        fillExistingUserData()

        saveUserDetailsOnClickListener()

        return root
    }

    private fun saveUserDetailsOnClickListener() {
        validateUserDetailsField()
        saveUserDetailsButton.setOnClickListener {
            userRepository.updateUserDetails(
                PMAGApp.fUser?.email,
                firstNameInputLayout.editText?.text.toString(),
                lastNameInputLayout.editText?.text.toString(),
                phoneNumberInputLayout.editText?.text.toString(),
                cityInputLayout.editText?.text.toString()
            )
            Toast.makeText(PMAGApp.ctx, "User details updated successfully", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateUserDetailsField() {

    }

    private fun getFields(root: View) {
        emailAddressTextView = root.findViewById(R.id.emailAddressTextView)
        firstNameInputLayout = root.findViewById(R.id.firstNameTextInput)
        lastNameInputLayout = root.findViewById(R.id.lastNameTextInput)
        cityInputLayout = root.findViewById(R.id.cityTextInput)
        phoneNumberInputLayout = root.findViewById(R.id.phoneNumberTextInput)
        saveUserDetailsButton = root.findViewById(R.id.saveUserDetailsButton)
    }

    private fun fillExistingUserData() {
        userRepository.getUser(PMAGApp.fUser?.email)
            .addOnSuccessListener {
                val user = it.toObjects(User::class.java).first()
                emailAddressTextView.text = user.emailAddress
                firstNameInputLayout.editText?.setText(user.firstName)
                lastNameInputLayout.editText?.setText(user.lastName)
                cityInputLayout.editText?.setText(user.city)
                phoneNumberInputLayout.editText?.setText(user.phoneNumber)
            }
    }
}