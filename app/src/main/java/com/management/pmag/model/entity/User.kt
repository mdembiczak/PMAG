package com.management.pmag.model.entity

import java.io.Serializable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val phoneNumber: String = "",
    val emailAddress: String = "",
    val projectContext: String = ""
) : Serializable
