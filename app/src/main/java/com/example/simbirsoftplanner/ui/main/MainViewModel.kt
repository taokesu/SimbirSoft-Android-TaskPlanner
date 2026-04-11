package com.example.simbirsoftplanner.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.data.repository.TaskRepository
import com.example.simbirsoftplanner.util.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class MainViewModel(
    private val repository: TaskRepository,
    application: Application
) : AndroidViewModel(application) {

    private var searchJob: kotlinx.coroutines.Job? = null

    var currentSelectedDate: Long = System.currentTimeMillis()
        private set

    private val _filteredTasks = MutableLiveData<List<TaskEntity>>()
    val filteredTasks: LiveData<List<TaskEntity>> = _filteredTasks

    init {
        checkAndLoadInitialData()
        loadTasksForDate(currentSelectedDate)
    }

    private fun checkAndLoadInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTasks = repository.allTasks.firstOrNull()
            if (currentTasks.isNullOrEmpty()) {
                val tasksFromJson = JsonUtils.loadTasksFromAssets(getApplication())
                repository.insertAll(tasksFromJson)
            }
        }
    }

    fun onDateSelected(timestampMs: Long) {
        currentSelectedDate = timestampMs
        loadTasksForDate(timestampMs)
    }

    fun loadTasksForDate(timestampMs: Long) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestampMs

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis

            val endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1

            Log.d("DEBUG_RANGE", "Ищем задачи локально: $startOfDay - $endOfDay")

            repository.getTasksForDay(startOfDay, endOfDay).collect { tasks ->
                Log.d("DEBUG_UI", "ViewModel: найдено задач: ${tasks.size}")
                _filteredTasks.postValue(tasks)
            }
        }
    }
}