package com.example.simbirsoftplanner.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simbirsoftplanner.PlannerApp
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.databinding.ActivityTaskDetailBinding
import com.example.simbirsoftplanner.ui.create.AddTaskActivity
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var currentTask: TaskEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentTask = intent.getSerializableExtra("TASK") as? TaskEntity
            ?: run { finish(); return }

        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("ru"))
        val startDate = Date(currentTask.dateStart)

        binding.tvDetailTitle.text = currentTask.name
        binding.tvDetailDateTime.text = "${dateFormat.format(startDate)} — ${
            SimpleDateFormat(
                "HH:mm",
                Locale("ru")
            ).format(Date(currentTask.dateFinish))
        }"
        binding.tvDetailDescription.text = currentTask.description.ifEmpty { "Нет описания" }

        supportActionBar?.title = "Подробности задачи"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnEditTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("TASK_TO_EDIT", currentTask)
            startActivity(intent)
            finish()
        }

        binding.btnDeleteTask.setOnClickListener {
            val repository = (application as PlannerApp).repository
            runBlocking {
                repository.deleteTask(currentTask)
            }
            Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}