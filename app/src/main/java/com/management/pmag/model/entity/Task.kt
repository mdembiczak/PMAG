package com.management.pmag.model.entity

import java.io.Serializable

data class Task(
    val taskTag: String = "",
    val projectTag: String = "",
    val title: String = "",
    val description: String = "",
    val state: String = "",
    val taskCreatorId: String = "",
    val assignedTo: String = "",
    val creationData: String = "",
    val dueDate: String = ""
) : Serializable