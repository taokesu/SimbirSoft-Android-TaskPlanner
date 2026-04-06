package com.example.simbirsoftplanner

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoftplanner.ui.main.MainViewModel
import com.example.simbirsoftplanner.ui.main.MainViewModelFactory
import com.example.simbirsoftplanner.databinding.ActivityMainBinding
import com.example.simbirsoftplanner.ui.main.TaskAdapter
import kotlinx.coroutines.launch
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
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = taskAdapter

        setupCalendar()
        observeViewModel()
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            viewModel.onDateSelected(calendar.timeInMillis)
        }
    }

    private fun observeViewModel() {
        viewModel.filteredTasks.observe(this) { taskList ->
            taskAdapter.setItems(taskList)
        }
    }
}