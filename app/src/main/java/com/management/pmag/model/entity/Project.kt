package com.management.pmag.model.entity

data class Project(
    val projectId: String,
    val projectName: String,
    val projectDescription: String,
    val projectOwnerId: Int
)