package com.management.pmag.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProjectModel(@SerializedName("projectId") @Expose val projectId: String) {
}