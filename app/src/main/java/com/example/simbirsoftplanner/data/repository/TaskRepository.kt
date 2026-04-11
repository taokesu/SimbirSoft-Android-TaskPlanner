package com.example.simbirsoftplanner.data.repository

import android.content.Context
import com.example.simbirsoftplanner.data.local.TaskDao
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.util.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskDao: TaskDao,
    private val context: Context
) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()

    fun getTasksForDay(start: Long, end: Long): Flow<List<TaskEntity>> {
        return taskDao.getTasksForDay(start, end)
    }

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun insertAll(tasks: List<TaskEntity>) {
        taskDao.insertAll(tasks)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }
}