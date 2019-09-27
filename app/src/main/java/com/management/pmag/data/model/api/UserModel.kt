package com.management.pmag.data.model.api

/**
 * Data class that captures userModel information for logged in users retrieved from LoginRepository
 */
data class UserModel(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val addressId: String,
    val phoneNumber: String,
    val emailAddress: String
){



}
