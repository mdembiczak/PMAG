package com.management.pmag.ui.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
    lateinit var button: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        projectViewModel =
            ViewModelProviders.of(this).get(ProjectViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_project, container, false)
        val textView: TextView = root.findViewById(R.id.text_send)

        button = root.findViewById(R.id.button)

        button.setOnClickListener {
            val project = PMAGApp.fUser?.uid?.let { userId ->
                Project(
                    "testID",
                    "projectName",
                    "projectDescription",
                    userId
                )
            }
            project?.let { existingProject -> val dbLog = projectRepository.save(existingProject)
                Toast.makeText(PMAGApp.ctx, dbLog, Toast.LENGTH_SHORT).show()
            }
        }

        projectViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }


}