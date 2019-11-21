package com.management.pmag.ui.project.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Project

class ProjectAdapter(private val dataSource: ArrayList<Project>) : BaseAdapter() {
    private val inflater: LayoutInflater =
        PMAGApp.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.project_list_item, parent, false)
        val projectName = rowView.findViewById(R.id.projectNameTextView) as TextView
        val projectTag = rowView.findViewById(R.id.projectTagTextView) as TextView
        val projectOwner = rowView.findViewById(R.id.projectOwnerTextView) as TextView

        val project = getItem(position) as Project
        projectName.text = project.projectName
        projectTag.text = project.projectTag
        projectOwner.text = project.projectOwnerId

        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }
}