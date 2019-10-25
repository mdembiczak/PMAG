package com.management.pmag.ui.project.details

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Project

class ProjectDetailsActivity : AppCompatActivity() {

    private lateinit var projectName: EditText
    private lateinit var projectTag: TextView
    private lateinit var projectStateSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)

        projectName = findViewById(R.id.editableProjectNameId)
        projectTag = findViewById(R.id.staticProjectTagId)
        projectStateSpinner = findViewById(R.id.projectStateSpinner)
        initializeProjectStateSpinner(projectStateSpinner)

        val project = intent.getSerializableExtra("PROJECT") as Project?

        projectName.setText(project?.projectName)
        projectTag.text = project?.projectTag
    }

    private fun initializeProjectStateSpinner(projectStateSpinner: Spinner) {
        ArrayAdapter.createFromResource(
            PMAGApp.ctx,
            R.array.project_state_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            projectStateSpinner.adapter = adapter
        }
    }
}
