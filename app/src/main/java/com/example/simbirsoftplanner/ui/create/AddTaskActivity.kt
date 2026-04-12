package com.example.simbirsoftplanner.ui.create

import android.app.TimePickerDialog
import java.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.simbirsoftplanner.PlannerApp
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.databinding.ActivityAddTaskBinding


class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private var selectedTimestamp: Long = 0
    private var selectedHour: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTimePicker()

        val taskToEdit = intent.getSerializableExtra("TASK_TO_EDIT") as? TaskEntity

        selectedTimestamp = intent.getLongExtra("SELECTED_DATE", System.currentTimeMillis())

        val repository = (application as PlannerApp).repository
        val factory = AddTaskViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(AddTaskViewModel::class.java)

        if (taskToEdit != null) {
            supportActionBar?.title = "Редактирование задачи"
            binding.etTaskName.setText(taskToEdit.name)
            binding.etTaskDescription.setText(taskToEdit.description)

            val cal = Calendar.getInstance().apply { timeInMillis = taskToEdit.dateStart }
            selectedHour = cal.get(Calendar.HOUR_OF_DAY)
            binding.tvSelectedTime.text = String.format("Выбрано время: %02d:00", selectedHour)
        }

        binding.btnSaveTask.setOnClickListener {
            val name = binding.etTaskName.text.toString()
            val desc = binding.etTaskDescription.text.toString()

            if (name.isNotEmpty()) {
                if (taskToEdit != null) {
                    viewModel.updateTask(
                        taskToEdit.copy(
                            name = name,
                            description = desc,
                            dateStart = taskToEdit.dateStart
                        )
                    )
                } else {
                    viewModel.saveTask(name, desc, selectedTimestamp, selectedHour)
                }
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