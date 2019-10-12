package com.management.pmag.model.entity

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val taskOwnerId: Int,
    val assignedTo: String,
    val creationData: String,
    val commentsId: String
)