package com.management.pmag.ui.project.utils

import android.widget.Button
import com.management.pmag.model.entity.Project
import com.management.pmag.model.repository.UserRepository

object ProjectUtils {
    private const val projectStatus = "Open"

    private val userRepository = UserRepository()
    fun setProjectAsDefaultOnClickListener(button: Button, projectTag: String, email: String) {
        button.setOnClickListener {
            userRepository.updateProjectContext(email, projectTag)
        }
    }

    fun projectCreation(
        projectTag: String,
        projectName: String,
        projectDescription: String,
        userId: String,
        email: String
    ): Project {
        return Project(
            projectTag, projectName, projectDescription, projectStatus, userId, email
        )
    }
}