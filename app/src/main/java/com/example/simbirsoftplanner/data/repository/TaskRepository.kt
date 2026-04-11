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
    // Получение всех задач
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()

    // Получение задач на конкретный день
    fun getTasksForDay(start: Long, end: Long): Flow<List<TaskEntity>> {
        return taskDao.getTasksForDay(start, end)
    }

    // Сохранение ОДНОЙ задачи (используется в AddTaskViewModel)
    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    // Загрузка первичных данных из JSON (используется в MainViewModel)
    suspend fun insertAll(tasks: List<TaskEntity>) {
        taskDao.insertAll(tasks)
    }
}