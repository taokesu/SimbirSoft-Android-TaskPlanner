package com.example.simbirsoftplanner.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.data.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AddTaskViewModel(private val repository: TaskRepository) : ViewModel() {
    fun saveTask(name: String, description: String, timestampMs: Long, hour: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestampMs
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val finalTimestamp = calendar.timeInMillis

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newTask = TaskEntity(
                    name = name,
                    description = description,
                    dateStart = finalTimestamp,
                    dateFinish = finalTimestamp + 3600000
                )
                repository.insertTask(newTask)
                Log.d("DEBUG_SAVE", "УСПЕШНО СОХРАНЕНО: $name")
            } catch (e: Exception) {
                Log.e("DEBUG_SAVE", "ОШИБКА: ${e.message}")
            }
        }
    }
}