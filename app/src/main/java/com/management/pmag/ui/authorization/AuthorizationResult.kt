package com.management.pmag.ui.authorization

import com.management.pmag.ui.authorization.login.LoggedInUserView

/**
 * Authentication result : success (userModel details) or error message.
 */
data class AuthorizationResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
