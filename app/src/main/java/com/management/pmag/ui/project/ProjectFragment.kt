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

    private lateinit var ownedProjectListView: ListView
    private lateinit var assignedProjectListView: ListView
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var notAssignedToProject: TextView
    private lateinit var addProjectFloatingButton: FloatingActionButton
    private val projectRepository: ProjectRepository = ProjectRepository()

    private val projectExtra = "PROJECT"
    private val emptyString = ""
    private lateinit var ownedProjectAdapter: ArrayAdapter<String>
    private lateinit var assignedProjectAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_project, container, false)
        val textView: TextView = root.findViewById(R.id.text_send)
        notAssignedToProject = root.findViewById(R.id.notAssignToProject)

        ownedProjectListView = root.findViewById(R.id.ownedProjectListView)
        assignedProjectListView = root.findViewById(R.id.assignedProjectListView)
        //add second list for user which are not project owners

        onClickProjectListItem()
        loadProjectsToOwnedProjectListView(ownedProjectListView)
        loadProjectsToAssignedProjectListView(assignedProjectListView)


        addProjectFloatingButton = root.findViewById(R.id.addProjectFloatingButton)
        addProjectFloatingButton.setOnClickListener {
            startActivity(Intent(PMAGApp.ctx, ProjectCreationActivity::class.java))
        }

        projectViewModel =
            ViewModelProviders.of(this).get(ProjectViewModel::class.java)


        projectViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    private fun onClickProjectListItem() {
        ownedProjectListView.setOnItemClickListener { _, view, _, _ ->
            val projectTagTextView: TextView = view.findViewById(R.id.label)
            val projectTag = projectTagTextView.text.toString()
            projectRepository.getProjectByProjectTag(projectTag)
                .addOnSuccessListener { result ->
                    val project = result.toObjects(Project::class.java).first()
                    if (project.projectOwnerId == PMAGApp.firebaseAuth.currentUser?.uid) {
                        val projectDetailsIntent =
                            Intent(context, ProjectDetailsActivity::class.java)
                        projectDetailsIntent.putExtra(projectExtra, project)
                        startActivity(projectDetailsIntent)
                    } else {
//                        val projectDetailsReadOnlyIntent =
//                            Intent(context, ProjectDetailsReadOnlyActivity::class.java)
//                        projectDetailsReadOnlyIntent.putExtra(projectExtra, project)
//                        startActivity(projectDetailsReadOnlyIntent)
                    }
                }
        }
    }

    private fun loadProjectsToOwnedProjectListView(ownedProjectListView: ListView) {
        projectRepository.getProjectsByOwnerId()
            .addSnapshotListener { snapshot, exception ->
                val projects = updateProjectListView(exception, snapshot, "Owned project List")
                if (projects.isNotEmpty()) {
                    Log.d(TAG, "User has projects as owner")
                    ownedProjectAdapter = ArrayAdapter(PMAGApp.ctx, R.layout.project_list_item)
                    ownedProjectAdapter.addAll(projects)
                    ownedProjectListView.adapter = ownedProjectAdapter
                }
            }
    }


    private fun loadProjectsToAssignedProjectListView(assignedProjectListView: ListView) {
        projectRepository.getProjectByUserEmail()
            .addSnapshotListener { snapshot, exception ->
                val projects = updateProjectListView(exception, snapshot, "Assigned Project List")
                if (projects.isNotEmpty()) {
                    Log.d(TAG, "User has assigned projects")
                    assignedProjectAdapter = ArrayAdapter(PMAGApp.ctx, R.layout.project_list_item)
                    assignedProjectAdapter.addAll(projects)
                    assignedProjectListView.adapter = assignedProjectAdapter
                }
            }
    }

    private fun updateProjectListView(
        exception: FirebaseFirestoreException?,
        snapshot: QuerySnapshot?,
        methodInfo: String
    ): List<String> {
        if (exception != null) {
            Log.w(TAG, "Listen failed", exception)
            return emptyList()
        }
        if (snapshot!!.isEmpty) {
            Log.d(TAG, "INFO: $methodInfo is empty")
        } else {
            notAssignedToProject.text = emptyString
            val projectList = snapshot.toObjects(Project::class.java)
            return projectList.stream().map { project ->
                project.projectTag
            }.collect(Collectors.toList())
        }
        return emptyList()
    }
}