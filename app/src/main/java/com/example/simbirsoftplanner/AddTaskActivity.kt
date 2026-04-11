package com.example.simbirsoftplanner

import android.app.TimePickerDialog
import java.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.simbirsoftplanner.data.local.AppDatabase
import com.example.simbirsoftplanner.data.repository.TaskRepository
import com.example.simbirsoftplanner.databinding.ActivityAddTaskBinding
import com.example.simbirsoftplanner.ui.main.AddTaskViewModel
import com.example.simbirsoftplanner.ui.main.AddTaskViewModelFactory


class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private var selectedTimestamp: Long = 0
    private var selectedHour: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTimePicker()

        selectedTimestamp = intent.getLongExtra("SELECTED_DATE", System.currentTimeMillis())

        val repository = (application as PlannerApp).repository
        val factory = AddTaskViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(AddTaskViewModel::class.java)

        binding.btnSaveTask.setOnClickListener {
            val name = binding.etTaskName.text.toString()
            val desc = binding.etTaskDescription.text.toString()

            if (name.isNotEmpty()) {
                Log.d("DEBUG", "Нажата кнопка сохранить. Время: $selectedTimestamp, Час: $selectedHour")
                viewModel.saveTask(name, desc, selectedTimestamp, selectedHour)
                finish()
            }
        }
    }

    private fun setupTimePicker() {
        binding.btnSelectTime.setOnClickListener {
            val timePicker = TimePickerDialog(this, { _, hourOfDay, _ ->
                this.selectedHour = hourOfDay
                binding.tvSelectedTime.text = String.format("Выбрано время: %02d:00", hourOfDay)
            }, 12, 0, true)
            timePicker.show()
        }
    }
}