package com.example.simbirsoftplanner.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoftplanner.data.model.TaskEntity
import com.example.simbirsoftplanner.databinding.ItemTaskBinding
import java.util.Calendar

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<TaskEntity> = emptyList()

    var onTaskClick: ((TaskEntity) -> Unit)? = null

    fun setItems(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = 24

    override fun onBindViewHolder(holder: TaskViewHolder, hour: Int) {
        holder.bind(hour, tasks)
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val clickedHour = bindingAdapterPosition
                val task = tasks.find { task ->
                    val calStart = Calendar.getInstance().apply { timeInMillis = task.dateStart }
                    val calEnd = Calendar.getInstance().apply { timeInMillis = task.dateFinish }
                    val startHour = calStart.get(Calendar.HOUR_OF_DAY)
                    val endHour = calEnd.get(Calendar.HOUR_OF_DAY)
                    clickedHour in startHour until endHour
                }
                task?.let { onTaskClick?.invoke(it) }
            }
        }

        fun bind(hour: Int, allTasks: List<TaskEntity>) {
            binding.tvTime.text = String.format("%02d:00-%02d:00", hour, hour + 1)

            val taskForThisHour = allTasks.find { task ->
                val calStart = Calendar.getInstance().apply { timeInMillis = task.dateStart }
                val calEnd = Calendar.getInstance().apply { timeInMillis = task.dateFinish }
                val startHour = calStart.get(Calendar.HOUR_OF_DAY)
                val endHour = calEnd.get(Calendar.HOUR_OF_DAY)
                hour in startHour until endHour
            }

            if (taskForThisHour != null) {
                binding.tvTaskName.text = taskForThisHour.name
                binding.tvTaskName.setTextColor(android.graphics.Color.BLACK)
            } else {
                binding.tvTaskName.text = "Свободно"
                binding.tvTaskName.setTextColor(android.graphics.Color.GRAY)
            }
        }
    }
}