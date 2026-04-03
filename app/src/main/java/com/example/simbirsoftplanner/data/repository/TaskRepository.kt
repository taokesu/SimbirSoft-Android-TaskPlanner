package com.example.simbirsoftplanner.data.repository

import android.content.Context
import com.example.simbirsoftplanner.data.local.TaskDao
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskDao: TaskDao,
    private val context: Context
) {
    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>> {
        return taskDao.getTasksForDay(startOfDay, endOfDay)
    }

    suspend fun populateDatabaseFromJson() = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("tasks.json").bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<TaskEntity>>() {}.type
            val tasks: List<TaskEntity> = Gson().fromJson(jsonString, listType)

            taskDao.insertTasks(tasks)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }
}