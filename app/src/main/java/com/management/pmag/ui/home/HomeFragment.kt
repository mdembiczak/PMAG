package com.management.pmag.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.management.pmag.PMAGApp
import com.management.pmag.R
import com.management.pmag.model.entity.Task
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.TaskRepository
import com.management.pmag.model.repository.UserRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

        val calendarView: CalendarView = root.findViewById(R.id.calendarView)

        val calendar = Calendar.getInstance()
        val calMin: Calendar = Calendar.getInstance()
        calMin.set(2019, 0, 1)
        val calMax: Calendar = Calendar.getInstance().also {
            it.set(2030, 0, 1)
        }

        calendarView.setMinimumDate(calMin)
        calendarView.setMaximumDate(calMax)
        calendarView.setDate(calendar)

        userRepository.getUserQuery(PMAGApp.firebaseAuth.currentUser?.email!!)
            .get()
            .addOnSuccessListener { userQuery ->
                val user = userQuery?.toObjects(User::class.java)?.first()!!
                taskRepository.getQueryTaskByProjectTag(user.projectContext)
                    .get()
                    .addOnSuccessListener { taskQuery ->
                        taskList = taskQuery?.toObjects(Task::class.java)?.toList()!!
                        if (taskList.isNotEmpty()) {
                            val events: ArrayList<EventDay> = ArrayList()
                            taskList.forEach {
                                val calendarToEvent = Calendar.getInstance()
                                val date =
                                    LocalDate.parse(it.creationData, DateTimeFormatter.ISO_DATE)
                                calendarToEvent.set(date.year, date.monthValue - 1, date.dayOfMonth)
                                events.add(EventDay(calendarToEvent, R.drawable.ic_launcher))
                            }
                            calendarView.setEvents(events)
                        }
                    }
            }


        calendarView.setOnDayClickListener {
            val time = it.calendar.time.toString()
            val split = time.split(" ")
            val calendarDate = "${split[2]}-${mapDayName(split[1])}-${split[5]}"
        }


        return root
    }

    private fun mapDayName(name: String): Int {
        when (name) {
            "Jan" -> return 1
            "Feb" -> return 2
            "Mar" -> return 3
            "Apr" -> return 4
            "May" -> return 5
            "Jun" -> return 6
            "Jul" -> return 7
            "Aug" -> return 8
            "Sep" -> return 9
            "Oct" -> return 10
            "Nov" -> return 11
            "Dec" -> return 12
        }
        return 0
    }
}