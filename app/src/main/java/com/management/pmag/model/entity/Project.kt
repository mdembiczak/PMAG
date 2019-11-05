package com.management.pmag.model.entity

import java.io.Serializable

data class Project(
    val projectTag: String = "",
    val projectName: String = "",
    val projectDescription: String = "",
    val projectStatus: String = "",
    val createdBy: String = "",
    val projectOwnerId: String = ""
) : Serializable

