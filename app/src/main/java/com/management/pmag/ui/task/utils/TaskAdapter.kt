package com.management.pmag.ui.task.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.management.pmag.R
import com.management.pmag.model.entity.Task

class TaskAdapter(
    context: Context,
    private val dataSource: ArrayList<Task>
) : BaseAdapter() {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.task_list_item, parent, false)
        val taskTitle = rowView.findViewById(R.id.taskTitleTextView) as TextView
        val taskTag = rowView.findViewById(R.id.taskTagTextView) as TextView
        val taskCreatedAt = rowView.findViewById(R.id.taskCreatedAtTextView) as TextView

        val task = getItem(position) as Task
        taskTitle.text = task.title
        taskTag.text = task.taskTag
        taskCreatedAt.text = task.creationData

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