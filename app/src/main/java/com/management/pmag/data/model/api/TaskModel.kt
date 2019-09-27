package com.management.pmag.data.model.api

data class TaskModel(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val ownerId: Long,
    val assignedTo: String,
    val creationData: String,
    val commentsId: String
)