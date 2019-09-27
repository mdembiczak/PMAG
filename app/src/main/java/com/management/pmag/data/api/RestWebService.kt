package com.management.pmag.data.api

import com.management.pmag.data.model.api.AddressModel
import com.management.pmag.data.model.api.ProjectModel
import com.management.pmag.data.model.api.TaskModel
import com.management.pmag.data.model.api.UserModel
import retrofit2.Callback
import retrofit2.http.GET

interface RestWebService {

    @GET(ConstantPaths.PROJECTS_PATH)
    fun getProject(response: Callback<ProjectModel>)

    @GET(ConstantPaths.TASK_PATH)
    fun getTasks(response: Callback<TaskModel>)

    @GET(ConstantPaths.ADDRESS_PATH)
    fun getAddress(response: Callback<AddressModel>)

    @GET(ConstantPaths.USER_PATH)
    fun getUser(response: Callback<UserModel>)

    class Factory {
        companion object : RestWebService {
            override fun getProject(response: Callback<ProjectModel>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getTasks(response: Callback<TaskModel>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAddress(response: Callback<AddressModel>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getUser(response: Callback<UserModel>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }
}