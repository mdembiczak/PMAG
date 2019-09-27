package com.management.pmag.data.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProjectModel(@SerializedName("projectId") @Expose val projectId: String)