package com.example.simbirsoftplanner.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.data.repository.TaskRepository
import com.example.simbirsoftplanner.util.JsonUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(
    private val repository: TaskRepository,
    private val application: Application
) : AndroidViewModel(application) {
    private val _filteredTasks = MutableLiveData<List<TaskEntity>>()
    val filteredTasks: LiveData<List<TaskEntity>> = _filteredTasks

    init {
        checkAndLoadInitialData()
    }

    private fun checkAndLoadInitialData() {
        viewModelScope.launch {
            val currentTasks = repository.allTasks.firstOrNull()
            if (currentTasks.isNullOrEmpty()) {
                val tasksFromJson = JsonUtils.loadTasksFromAssets(getApplication())
                repository.insertTasks(tasksFromJson)
            }
        }
    }

    fun onDateSelected(timestamp: Long) {
        loadTasksForDate(timestamp)
    }

    fun loadTasksForDate(timestamp: Long) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            val startOfDay = calendar.timeInMillis / 1000

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            val endOfDay = calendar.timeInMillis / 1000

            repository.getTasksForDay(startOfDay, endOfDay).collect { tasks ->
                _filteredTasks.value = tasks
            }
        }
    }
}