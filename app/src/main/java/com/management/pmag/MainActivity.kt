package com.management.pmag

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.management.pmag.controller.login.LoginDataSource
import com.management.pmag.model.entity.User
import com.management.pmag.model.repository.LoginRepository
import com.management.pmag.model.repository.UserRepository
import com.management.pmag.ui.authorization.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val loginRepository: LoginRepository = LoginRepository(dataSource = LoginDataSource())
    private val userRepository: UserRepository = UserRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_board, R.id.nav_task,
                R.id.nav_settings, R.id.nav_share, R.id.nav_project
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        initializeNavView(navView)
    }

    private fun initializeNavView(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val emailTextView = headerView.findViewById<TextView>(R.id.emailAddressTextView)
        val fullNameText = headerView.findViewById<TextView>(R.id.fullNameTextViewId)

        userRepository.getUserQuery(PMAGApp.firebaseAuth.currentUser?.email)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObjects(User::class.java)?.first()
                emailTextView.text = user?.emailAddress.orEmpty()
                val fullName = user?.firstName + " " + user?.lastName
                fullNameText.text = fullName
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_logout -> {
            loginRepository.logout()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
