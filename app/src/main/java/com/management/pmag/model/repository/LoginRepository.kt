package com.management.pmag.model.repository

import com.management.pmag.controller.login.LoginDataSource
import com.management.pmag.model.entity.User

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {
    var user: User? = null
        private set

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }
}
