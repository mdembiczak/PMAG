package com.management.pmag.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.management.pmag.R
import com.management.pmag.model.repository.TaskRepository

class TaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskListView: ListView

    private val taskRepository = TaskRepository()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskViewModel =
            ViewModelProviders.of(this).get(TaskViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        taskViewModel.text.observe(this, Observer {
            textView.text = it
        })

        taskListView = root.findViewById(R.id.taskListView)

        return root
    }

//    private fun onClickProjectListItem() {
//        taskListView.setOnItemClickListener { _, view, _, _ ->
//            val projectTagTextView: TextView = view.findViewById(R.id.taskTagTextView)
//            val projectTag = projectTagTextView.text.toString()
//            taskRepository.getProjectByProjectTag(projectTag = projectTag)
//                .addOnSuccessListener { result ->
//                    val project = result.toObjects(Project::class.java).first()
//                    val projectDetailsIntent = Intent(context, ProjectDetailsActivity::class.java)
//                    projectDetailsIntent.putExtra(projectExtra, project)
//                    startActivity(projectDetailsIntent)
//                }
//        }
//    }
//
//    private fun loadProjectsToProjectListView(projectListView: ListView) {
//        taskRepository.getProjectsByOwnerId()
//            .addSnapshotListener { snapshot, exception ->
//                updateProjectListView(exception, snapshot, projectListView)
//            }
//    }
//
//    private fun updateProjectListView(
//        exception: FirebaseFirestoreException?,
//        snapshot: QuerySnapshot?,
//        projectListView: ListView
//    ) {
//        if (exception != null) {
//            Log.w(ContentValues.TAG, "Listen failed", exception)
//            return
//        }
//        if (snapshot!!.isEmpty) {
//            Log.e(ContentValues.TAG, "Project list is empty.")
//            notAssignedToProject.text = "You are not assign to project"
//        } else {
//            notAssignedToProject.text = emptyString
//            val projectList = snapshot.toObjects(Project::class.java)
//            val projectTagList = projectList.stream().map { project ->
//                project.projectTag
//            }.collect(Collectors.toList())
//
//            val adapter =
//                ArrayAdapter(PMAGApp.ctx, R.layout.project_list_item, projectTagList)
//            projectListView.adapter = adapter
//        }
//    }
}