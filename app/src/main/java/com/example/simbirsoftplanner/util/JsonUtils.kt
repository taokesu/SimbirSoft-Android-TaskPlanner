package com.example.simbirsoftplanner.util

import android.content.Context
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonUtils {
    fun loadTasksFromAssets(context: Context): List<TaskEntity> {
        return try {
            val jsonString = context.assets.open("tasks.json")
                .bufferedReader()
                .use { it.readText() }

            val listType = object : TypeToken<List<TaskEntity>>() {}.type
            Gson().fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}