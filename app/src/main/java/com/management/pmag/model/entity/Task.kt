package com.management.pmag.model.entity

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val ownerId: Long,
    val assignedTo: String,
    val creationData: String,
    val commentsId: String
)