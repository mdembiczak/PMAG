package com.management.pmag.ui.authorization.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.management.pmag.R
import com.management.pmag.ui.authorization.AuthorizationResult
import com.management.pmag.ui.authorization.AuthorizationState
import com.management.pmag.ui.authorization.AuthorizationValidation

class LoginViewModel() : ViewModel() {

    private val _loginForm = MutableLiveData<AuthorizationState>()
    val loginFormState: LiveData<AuthorizationState> = _loginForm

    private val _loginResult = MutableLiveData<AuthorizationResult>()
    val loginFormResult: LiveData<AuthorizationResult> = _loginResult

    private val authorizationValidation = AuthorizationValidation()

    fun loginDataChanged(username: String, password: String) {
        if (!authorizationValidation.isUserNameValid(username)) {
            _loginForm.value =
                AuthorizationState(usernameError = R.string.invalid_username)
        } else if (!authorizationValidation.isPasswordValid(password)) {
            _loginForm.value =
                AuthorizationState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value =
                AuthorizationState(isDataValid = true)
        }
    }

}
