package com.management.pmag.model.entity

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val addressId: String,
    val phoneNumber: String,
    val emailAddress: String
)
