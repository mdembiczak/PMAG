package com.management.pmag.ui.project

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.management.pmag.ui.project.common.ProjectDetailsGuestActivity
import com.management.pmag.ui.project.common.ProjectDetailsOwnerActivity
import com.management.pmag.ui.project.utils.ProjectAdapter

class ProjectFragment : Fragment() {
    private lateinit var ownedProjectListView: ListView
    private lateinit var assignedProjectListView: ListView
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var notAssignedToProject: TextView
    private lateinit var addProjectFloatingButton: FloatingActionButton
    private lateinit var ownedProjectAdapter: ProjectAdapter
    private lateinit var assignedProjectAdapter: ProjectAdapter

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
        init(root)

        onClickProjectListItem(ownedProjectListView)
        onClickProjectListItem(assignedProjectListView)
        loadProjectsToOwnedProjectListView(ownedProjectListView)
        loadProjectsToAssignedProjectListView(assignedProjectListView)

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

    private fun init(root: View) {
        notAssignedToProject = root.findViewById(R.id.notAssignToProject)
        ownedProjectListView = root.findViewById(R.id.ownedProjectListView)
        assignedProjectListView = root.findViewById(R.id.assignedProjectListView)
        addProjectFloatingButton = root.findViewById(R.id.addProjectFloatingButton)
    }

    private fun onClickProjectListItem(listView: ListView) {
        listView.setOnItemClickListener { _, view, _, _ ->
            val projectTagTextView: TextView = view.findViewById(R.id.projectTagTextView)
            val projectTag = projectTagTextView.text.toString()
            projectRepository.getProjectByProjectTag(projectTag)
                .addOnSuccessListener {
                    val project = it.toObjects(Project::class.java).first()
                    if (project.projectOwnerId.equals(PMAGApp.firebaseAuth.currentUser?.email!!)) {
                        val projectDetailsIntent =
                            Intent(context, ProjectDetailsOwnerActivity::class.java)
                        projectDetailsIntent.putExtra(projectExtra, project)
                        startActivity(projectDetailsIntent)
                    } else {
                        val projectDetailsReadOnlyIntent =
                            Intent(context, ProjectDetailsGuestActivity::class.java)
                        projectDetailsReadOnlyIntent.putExtra(projectExtra, project)
                        startActivity(projectDetailsReadOnlyIntent)
                    }
                }
        }
    }

    private fun loadProjectsToOwnedProjectListView(ownedProjectListView: ListView) {
        projectRepository.getProjectsByOwnerId(PMAGApp.firebaseAuth.currentUser?.email!!)
            .addSnapshotListener { snapshot, exception ->
                val projectList = updateProjectListView(exception, snapshot, "Owned project List")
                val projectArrayList = ArrayList(projectList)
                if (projectList.isNotEmpty()) {
                    Log.d(TAG, "User has projects as owner")
                    ownedProjectAdapter = ProjectAdapter(projectArrayList)
                    ownedProjectListView.adapter = ownedProjectAdapter
                }
            }
    }


    private fun loadProjectsToAssignedProjectListView(assignedProjectListView: ListView) {
        projectRepository.getProjectByUserEmail(PMAGApp.firebaseAuth.currentUser?.email!!)
            .addSnapshotListener { snapshot, exception ->
                val projectList =
                    updateProjectListView(exception, snapshot, "Assigned Project List")
                val projectArrayList = ArrayList(projectList)
                if (projectList.isNotEmpty()) {
                    Log.d(TAG, "User has assigned projects")
                    assignedProjectAdapter = ProjectAdapter(projectArrayList)
                    assignedProjectListView.adapter = assignedProjectAdapter
                }
            }
    }

    private fun updateProjectListView(
        exception: FirebaseFirestoreException?,
        snapshot: QuerySnapshot?,
        methodInfo: String
    ): List<Project> {
        if (exception != null) {
            Log.e(TAG, "Listen failed", exception)
            return emptyList()
        }
        if (snapshot!!.isEmpty) {
            Log.d(TAG, "INFO: $methodInfo is empty")
        } else {
            notAssignedToProject.text = emptyString
            return snapshot.toObjects(Project::class.java)
        }
        return emptyList()
    }
}