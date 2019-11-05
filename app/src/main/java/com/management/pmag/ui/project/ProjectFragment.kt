package com.management.pmag.ui.project

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Project
import com.management.pmag.model.repository.ProjectRepository
import com.management.pmag.ui.project.common.ProjectCreationActivity
import com.management.pmag.ui.project.common.ProjectDetailsActivity
import java.util.stream.Collectors

class ProjectFragment : Fragment() {

    private lateinit var projectListView: ListView
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var notAssignedToProject: TextView
    private lateinit var addProjectFloatingButton: FloatingActionButton
    private val projectRepository: ProjectRepository = ProjectRepository()

    private val projectExtra = "PROJECT"
    private val emptyString = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_project, container, false)
        val textView: TextView = root.findViewById(R.id.text_send)
        notAssignedToProject = root.findViewById(R.id.notAssignToProject)

        projectListView = root.findViewById(R.id.projectListView)
        loadProjectsToProjectListView(projectListView)

        addProjectFloatingButton = root.findViewById(R.id.addProjectFloatingButton)
        addProjectFloatingButton.setOnClickListener {
            startActivity(Intent(PMAGApp.ctx, ProjectCreationActivity::class.java))
        }

        projectViewModel =
            ViewModelProviders.of(this).get(ProjectViewModel::class.java)

        projectListView.setOnItemClickListener { _, view, _, _ ->
            val projectTagTextView: TextView = view.findViewById(R.id.label)
            val projectTag = projectTagTextView.text.toString()
            projectRepository.getProjectByProjectTag(projectTag = projectTag)
                .addOnSuccessListener { result ->
                    val project = result.toObjects(Project::class.java).first()
                    val projectDetailsIntent = Intent(context, ProjectDetailsActivity::class.java)
                    projectDetailsIntent.putExtra(projectExtra, project)
                    startActivity(projectDetailsIntent)
                }
        }

        projectViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    private fun loadProjectsToProjectListView(projectListView: ListView) {
        projectRepository.getProjectsByOwnerId()
            .addSnapshotListener { snapshot, exception ->
                updateProjectListView(exception, snapshot, projectListView)
            }
    }

    private fun updateProjectListView(
        exception: FirebaseFirestoreException?,
        snapshot: QuerySnapshot?,
        projectListView: ListView
    ) {
        if (exception != null) {
            Log.w(TAG, "Listen failed", exception)
            return
        }
        if (snapshot!!.isEmpty) {
            Log.e(TAG, "Project list is empty.")
            notAssignedToProject.text = "You are not assign to project"
        } else {
            notAssignedToProject.text = emptyString
            val projectList = snapshot.toObjects(Project::class.java)
            val projectTagList = projectList.stream().map { project ->
                project.projectTag
            }.collect(Collectors.toList())

            val adapter =
                ArrayAdapter(PMAGApp.ctx, R.layout.project_list_item, projectTagList)
            projectListView.adapter = adapter
        }
    }
}