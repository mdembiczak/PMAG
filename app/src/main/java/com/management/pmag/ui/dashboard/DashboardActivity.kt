package com.management.pmag.ui.dashboard

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.management.pmag.ProjectsFragment
import com.management.pmag.R
import com.management.pmag.dummy.ProjectsListTemporaryData

class DashboardActivity : AppCompatActivity(), ProjectsFragment.OnListFragmentInteractionListener {
    var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

    override fun onListFragmentInteraction(item: ProjectsListTemporaryData.DummyItem?) {
        fragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    }

//TODO: MD set valid fragments
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
//                fragmentTransaction.replace()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
//                fragmentTransaction.replace()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                fragmentTransaction.replace()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
