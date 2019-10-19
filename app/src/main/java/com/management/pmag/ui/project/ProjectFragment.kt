package com.management.pmag.ui.project

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Project
import com.management.pmag.model.repository.ProjectRepository

class ProjectFragment : Fragment() {

    private lateinit var projectViewModel: ProjectViewModel
    private val projectRepository: ProjectRepository = ProjectRepository()
    lateinit var addProjectButton: Button
    private lateinit var projectListView: ListView
    private lateinit var projectTag: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_project, container, false)
        val textView: TextView = root.findViewById(R.id.text_send)
        projectListView = root.findViewById(R.id.projectListView)
        loadProjects(projectListView)

        projectViewModel =
            ViewModelProviders.of(this).get(ProjectViewModel::class.java)

        addProjectButton = root.findViewById(R.id.addProjectButtonId)
        projectTag = root.findViewById(R.id.projectNameId)


        addProjectButton.setOnClickListener {
            val project =
                PMAGApp.fUser?.uid?.let { userId ->
                    buildProject(
                        userId,
                        projectTag.text.toString()
                    )
                }
            project?.let { existingProject -> projectRepository.saveProject(existingProject) }
        }

        projectViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    private fun buildProject(userId: String, projectTag: String): Project {
        return Project(projectTag, "name${projectTag}", "description", userId)
    }

    fun loadProjects(projectListView: ListView) {
        projectRepository.loadProjects()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.e(TAG, "Project list is empty.")
                } else {
                    val projectList = result.toObjects(Project::class.java)
                    val adapter = ArrayAdapter(PMAGApp.ctx, R.layout.project_list_item, projectList)
                    projectListView.adapter = adapter
                }
            }
    }


}