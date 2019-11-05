package com.management.pmag.ui.project.common

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Project
import com.management.pmag.model.repository.ProjectRepository

class ProjectCreationActivity : AppCompatActivity() {
    private val projectStatus = "Open"

    private lateinit var newProjectTag: TextInputLayout
    private lateinit var newProjectName: TextInputLayout
    private lateinit var newProjectDescription: TextInputLayout
    private lateinit var addNewProjectButton: FloatingActionButton
    private val projectRepository = ProjectRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_creation)

        newProjectTag = findViewById(R.id.newProjectTag)
        newProjectName = findViewById(R.id.newProjectName)
        newProjectDescription = findViewById(R.id.newProjectDescription)
        addNewProjectButton = findViewById(R.id.addProjectFloatingButton)

        addNewProjectButton.setOnClickListener {
            val projectTag = newProjectTag.editText?.text.toString()
            val projectName = newProjectName.editText?.text.toString()
            val projectDescription = newProjectDescription.editText?.text.toString()
            if (validateProjectFields(projectTag, projectName)) {
                addNewProjectButton.setOnClickListener {
                    val uid = PMAGApp.fUser?.uid
                    if (uid != null) {
                        val project =
                            projectCreation(
                                projectTag.toUpperCase(),
                                projectName,
                                projectDescription,
                                uid
                            )
                        project?.let { existingProject ->
                            projectRepository.saveProject(
                                existingProject
                            )
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun projectCreation(
        projectTag: String, projectName: String, projectDescription: String, userId: String
    ): Project {
        return Project(
            projectTag,
            projectName,
            projectDescription,
            projectStatus,
            userId,
            userId
        )
    }

    private fun validateProjectFields(projectTag: String, projectName: String): Boolean {
        when {
            projectTag.contains(Regex("[!@#\$%^&*(),.?\":{}|<>\\-]")) -> {
                Toast.makeText(
                    PMAGApp.ctx,
                    "You can't use special characters in Project TAG",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            projectName.length < 3 -> {
                Toast.makeText(
                    PMAGApp.ctx,
                    "Please set project name over 3 letters.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            projectName.length > 30 -> {
                Toast.makeText(
                    PMAGApp.ctx,
                    "Please set project name above 30 letters.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }
}
