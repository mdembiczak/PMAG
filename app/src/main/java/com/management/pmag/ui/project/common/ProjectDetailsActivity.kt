package com.management.pmag.ui.project.common

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Project
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.ProjectRepository
import com.management.pmag.model.repository.UserRepository

class ProjectDetailsActivity : AppCompatActivity() {

    private lateinit var projectTag: TextView
    private lateinit var projectOwner: TextView
    private lateinit var projectName: TextInputLayout
    private lateinit var projectStateSpinner: Spinner
    private lateinit var projectDescription: TextInputLayout
    private lateinit var saveProjectButton: Button
    private lateinit var setProjectAsDefaultButton: Button

    private val projectTagStaticText = "Project TAG: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)

        val userRepository = UserRepository()
        val projectRepository = ProjectRepository()

        projectTag = findViewById(R.id.staticProjectTagId)
        projectOwner = findViewById(R.id.projectOwnerTextView)
        projectName = findViewById(R.id.projectNameTextInputLayout)
        projectStateSpinner = findViewById(R.id.projectStateSpinner)
        projectDescription = findViewById(R.id.ProjectDescriptionTextInputLayout)
        saveProjectButton = findViewById(R.id.saveButton)
        setProjectAsDefaultButton = findViewById(R.id.setProjectAsDefaultId)


        val project = intent.getSerializableExtra("PROJECT") as Project?
        project?.projectStatus?.let { initializeProjectStateSpinner(projectStateSpinner, it) }

        val fullProjectTagDescription = projectTagStaticText + project?.projectTag
        projectTag.text = fullProjectTagDescription
        userRepository.getUser(PMAGApp.fUser?.email).addOnSuccessListener { result ->
            val user = result.toObjects(User::class.java).first()
            projectOwner.text = user.emailAddress
        }
        projectName.editText?.setText(project?.projectName)
        projectDescription.editText?.setText(project?.projectDescription)

        saveProjectButton.setOnClickListener {
            projectRepository.updateProject(
                project!!.projectTag,
                projectName.editText?.text.toString(),
                projectStateSpinner.selectedItem.toString(),
                projectDescription.editText?.text.toString()
            )
            finish()
        }

        setProjectAsDefaultButton.setOnClickListener {
            userRepository.updateProjectContext(PMAGApp.fUser?.email, project!!.projectTag)
        }
    }

    private fun initializeProjectStateSpinner(
        projectStateSpinner: Spinner,
        state: String = "default"
    ) {
        ArrayAdapter.createFromResource(
            PMAGApp.ctx,
            R.array.project_state_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            projectStateSpinner.adapter = adapter
            if (state != "default") {
                val position = adapter.getPosition(state)
                projectStateSpinner.setSelection(position)
            }
        }
    }
}
