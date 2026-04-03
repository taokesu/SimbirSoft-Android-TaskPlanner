package com.example.simbirsoftplanner.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks

    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate

    init {
        viewModelScope.launch {
            repository.populateDatabaseFromJson()
            loadTasksForDate(_selectedDate.value)
        }
    }

    fun onDateSelected(timestamp: Long) {
        _selectedDate.value = timestamp
        loadTasksForDate(timestamp)
    }

    private fun loadTasksForDate(timestamp: Long) {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        val endOfDay = calendar.timeInMillis

        viewModelScope.launch {
            repository.getTasksForDay(startOfDay, endOfDay).collect {
                _tasks.value = it
            }
        }
    }
}