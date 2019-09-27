package com.management.pmag.ui.authorization.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.management.pmag.data.LoginRepository
import com.management.pmag.data.Result

import com.management.pmag.R
import com.management.pmag.data.model.api.UserModel
import com.management.pmag.ui.authorization.AuthorizationResult
import com.management.pmag.ui.authorization.AuthorizationState
import com.management.pmag.ui.authorization.AuthorizationValidation

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<AuthorizationState>()
    val loginFormState: LiveData<AuthorizationState> = _loginForm

    private val _loginResult = MutableLiveData<AuthorizationResult>()
    val loginFormResult: LiveData<AuthorizationResult> = _loginResult

    private val authorizationValidation = AuthorizationValidation()

    fun login(username: String, password: String): Result<UserModel> {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            val displayName: String = result.data.firstName + " " + result.data.lastName
            _loginResult.value = AuthorizationResult(
                success = LoggedInUserView(displayName = displayName)
            )
        } else {
            _loginResult.value =
                AuthorizationResult(error = R.string.login_failed)
        }
        return result
    }

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
