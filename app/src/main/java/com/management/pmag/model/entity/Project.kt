package com.management.pmag.model.entity

data class Project(
    val projectTag: String,
    val projectName: String,
    val projectDescription: String,
    val projectOwnerId: String
)