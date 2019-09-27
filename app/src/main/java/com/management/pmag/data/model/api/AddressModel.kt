package com.management.pmag.data.model.api

data class AddressModel(
    val addressId: Long,
    val country: String,
    val city: String,
    val street: String,
    val streetNumber: String,
    val postalCode: String
)