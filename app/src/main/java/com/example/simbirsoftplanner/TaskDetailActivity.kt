package com.example.simbirsoftplanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simbirsoftplanner.databinding.ActivityTaskDetailBinding
import com.example.simbirsoftplanner.data.model.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val task = intent.getSerializableExtra("TASK") as? TaskEntity
        if (task == null) {
            finish()
            return
        }

        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("ru"))
        val startDate = Date(task.dateStart)
        val finishDate = Date(task.dateFinish)

        binding.tvDetailTitle.text = task.name
        binding.tvDetailDateTime.text = "${dateFormat.format(startDate)} — ${dateFormat.format(finishDate)}"
        binding.tvDetailDescription.text = task.description.ifEmpty { "Нет описания" }

        supportActionBar?.title = "Подробности задачи"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}