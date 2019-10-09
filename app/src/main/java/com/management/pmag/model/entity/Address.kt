package com.management.pmag.model.entity

data class Address(
    val addressId: Long,
    val country: String,
    val city: String,
    val street: String,
    val streetNumber: String,
    val postalCode: String
)