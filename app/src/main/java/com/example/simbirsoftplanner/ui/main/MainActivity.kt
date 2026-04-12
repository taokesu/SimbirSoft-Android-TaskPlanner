package com.example.simbirsoftplanner.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoftplanner.PlannerApp
import com.example.simbirsoftplanner.ui.detail.TaskDetailActivity
import com.example.simbirsoftplanner.databinding.ActivityMainBinding
import com.example.simbirsoftplanner.ui.create.AddTaskActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val taskAdapter = TaskAdapter()

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            (application as PlannerApp).repository,
            this.application
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = taskAdapter

        setupCalendar()
        observeViewModel()

        val today = System.currentTimeMillis()
        binding.calendarView.date = today
        viewModel.onDateSelected(today)

        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("SELECTED_DATE", viewModel.currentSelectedDate)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTasksForDate(viewModel.currentSelectedDate)
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val selectedMs = calendar.timeInMillis
            Log.d("DEBUG", "Выбрана дата (мс): $selectedMs")
            viewModel.onDateSelected(selectedMs)
        }
    }

    private fun observeViewModel() {
        viewModel.filteredTasks.observe(this) { tasks ->
            Log.d("DEBUG_UI", "MainActivity: отправляю в адаптер задач: ${tasks.size}")
            taskAdapter.setItems(tasks)
        }

        taskAdapter.onTaskClick = { task ->
            val intent = Intent(this, TaskDetailActivity::class.java)
            intent.putExtra("TASK", task)
            startActivity(intent)
        }
    }
}