package com.management.pmag.ui.project.common

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Project
import com.management.pmag.model.repository.ProjectRepository
import com.management.pmag.ui.project.utils.ProjectUtils

class ProjectDetailsGuestActivity : AppCompatActivity() {

    private lateinit var projectTag: TextView
    private lateinit var projectOwner: TextView
    private lateinit var projectName: TextView
    private lateinit var projectState: TextView
    private lateinit var projectDescription: TextInputLayout
    private lateinit var projectSaveButton: Button
    private lateinit var setAsDefaultButton: Button

    private val projectRepository = ProjectRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details_guest)

        val project = intent.getSerializableExtra("PROJECT") as Project

        init()
        fillFields(project)
        saveProjectOnClickListener()
        ProjectUtils.setProjectAsDefaultOnClickListener(
            setAsDefaultButton, projectTag.text.toString(),
            PMAGApp.firebaseAuth.currentUser?.email!!
        )
    }

    private fun init() {
        projectTag = findViewById(R.id.projectTagGuestTextView)
        projectOwner = findViewById(R.id.projectOwnerGuestTextView)
        projectName = findViewById(R.id.projectNameGuestTextView)
        projectState = findViewById(R.id.projectStateGuestTextView)
        projectDescription = findViewById(R.id.projectDescriptionTextInputLayout)
        projectSaveButton = findViewById(R.id.saveButtonGuest)
        setAsDefaultButton = findViewById(R.id.setProjectAsDefaultGuestButton)
    }

    private fun fillFields(project: Project) {
        projectTag.text = project.projectTag
        projectOwner.text = project.projectOwnerId
        projectName.text = project.projectName
        projectState.text = project.projectStatus
        projectDescription.editText?.setText(project.projectDescription)
    }

    private fun saveProjectOnClickListener() {
        projectSaveButton.setOnClickListener {
            projectRepository.updateProject(
                projectTag.text.toString(),
                projectName.text.toString(),
                projectState.text.toString(),
                projectDescription.editText?.text.toString()
            )
            finish()
        }
    }
}
