package com.management.pmag.ui.authorization

/**
 * Data validation state of the login form.
 */
data class AuthorizationState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
