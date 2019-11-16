package com.management.pmag.ui.project.common

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.*
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
    private lateinit var addProjectUserButton: Button
    private lateinit var projectUser: TextInputLayout

    private val projectRepository = ProjectRepository()
    private val userRepository = UserRepository()

    private val projectTagStaticText = "Project TAG: "
    private val emptyValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)

        projectTag = findViewById(R.id.staticProjectTagId)
        projectOwner = findViewById(R.id.projectOwnerTextView)
        projectName = findViewById(R.id.projectNameTextInputLayout)
        projectStateSpinner = findViewById(R.id.projectStateSpinner)
        projectDescription = findViewById(R.id.ProjectDescriptionTextInputLayout)
        saveProjectButton = findViewById(R.id.saveButton)
        setProjectAsDefaultButton = findViewById(R.id.setProjectAsDefaultId)
        addProjectUserButton = findViewById(R.id.addUserButton)
        projectUser = findViewById(R.id.userEmailAddressTextInputLayout)


        val project = intent.getSerializableExtra("PROJECT") as Project?
        project?.projectStatus?.let { initializeProjectStateSpinner(projectStateSpinner, it) }

        val fullProjectTagDescription = projectTagStaticText + project?.projectTag
        projectTag.text = fullProjectTagDescription
        userRepository.getUser(PMAGApp.firebaseAuth.currentUser?.email)
            .addOnSuccessListener { result ->
                val user = result.toObjects(User::class.java).first()
                projectOwner.text = user.emailAddress
            }
        projectName.editText?.setText(project?.projectName)
        projectDescription.editText?.setText(project?.projectDescription)

        saveProjectOnClickListener(project)
        setProjectAsDefaultOnClickListener(project)
        addProjectUserOnClickListener(project!!.projectTag)
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

    private fun saveProjectOnClickListener(project: Project?) {
        saveProjectButton.setOnClickListener {
            projectRepository.updateProject(
                project!!.projectTag,
                projectName.editText?.text.toString(),
                projectStateSpinner.selectedItem.toString(),
                projectDescription.editText?.text.toString()
            )
            finish()
        }
    }

    private fun setProjectAsDefaultOnClickListener(project: Project?) {
        setProjectAsDefaultButton.setOnClickListener {
            userRepository.updateProjectContext(
                PMAGApp.firebaseAuth.currentUser?.email,
                project!!.projectTag
            )
        }
    }

    private fun addProjectUserOnClickListener(projectTag: String) {
        addProjectUserButton.setOnClickListener {
            projectUser.editText?.text?.let { email ->
                userRepository.getUser(email.toString())
                    .addOnSuccessListener { query ->
                        if (query.isEmpty) {
                            Log.e(TAG, "User not with email: $email exist")
                        } else {
                            Log.d(TAG, "User with email $email exist")
                            projectRepository.updateProjectUser(projectTag, email.toString())
                            Toast.makeText(PMAGApp.ctx, "User added to project", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "ERROR: $e")
                    }
            }
            projectUser.editText?.setText(emptyValue)
        }
    }
}
