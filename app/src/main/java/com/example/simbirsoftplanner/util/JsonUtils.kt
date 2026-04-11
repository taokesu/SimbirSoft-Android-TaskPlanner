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
            val tasks: List<TaskEntity> = Gson().fromJson(jsonString, listType)

            tasks.map { task ->
                task.copy(
                    dateStart = task.dateStart * 1000,
                    dateFinish = task.dateFinish * 1000
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}