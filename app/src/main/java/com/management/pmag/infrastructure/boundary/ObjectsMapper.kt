package com.management.pmag.infrastructure.boundary

import com.google.gson.Gson

object ObjectsMapper{
    val gson: Gson = Gson()

    fun convertObjectToJson(commonObject: Any): String {
        return gson.toJson(commonObject)
    }
}