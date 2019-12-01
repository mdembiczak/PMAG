package com.management.pmag.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.model.repository.TaskRepository
import com.management.pmag.model.repository.UserRepository
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    private val taskRepository = TaskRepository()
    private val userRepository = UserRepository()

    private var dateList = emptyList<String>()
    private var taskList = emptyList<Task>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val calendar = Calendar.getInstance()
        var events: ArrayList<EventDay> = ArrayList()
        events.add(EventDay(calendar, R.drawable.ic_launcher))
        val calendarView: CalendarView = root.findViewById(R.id.calendarView)
        calendarView.setEvents(events)


//        customCalendar = root.findViewById(R.id.customCalendar)
//
//
//        userRepository.getUserQuery(PMAGApp.firebaseAuth.currentUser?.email!!)
//            .addSnapshotListener { userQuery, _ ->
//                val user = userQuery?.toObjects(User::class.java)?.first()!!
//                taskRepository.getQueryTaskByProjectTag(user.projectContext)
//                    .addSnapshotListener { taskQuery, _ ->
//                        taskList = taskQuery?.toObjects(Task::class.java)?.toList()!!
//                        dateList = taskList.map { it.creationData }
//                        if (dateList.isNotEmpty()) {
//                            for (i in dateList.indices) {
//                                customCalendar.addAnEvent(
//                                    dateList[i],
//                                    1,
//                                    arrayListOf(eventDataMapper(taskList[i]))
//                                )
//                            }
//                        }
//                    }
//            }

        return root
    }

//    fun eventDataMapper(task: Task): EventData {
//        val eventData = EventData()
//        val dataAboutDate = dataAboutDateMapper(task)
//        eventData.data = arrayListOf(dataAboutDate)
//        eventData.section = "Task"
//        return eventData
//    }
//
//    private fun dataAboutDateMapper(task: Task): dataAboutDate {
//        val dataAboutDate = dataAboutDate()
//        dataAboutDate.title = task.title
//        dataAboutDate.subject = task.taskTag
//        dataAboutDate.submissionDate = task.dueDate
//        dataAboutDate.remarks = ""
//        return dataAboutDate
//    }
}