package com.example.simbirsoftplanner

import android.app.Application
import androidx.room.Room
import com.example.simbirsoftplanner.data.local.AppDatabase
import com.example.simbirsoftplanner.data.repository.TaskRepository

class PlannerApp : Application() {
    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "planner_db").build()
    }

    val repository by lazy {
        TaskRepository(database.taskDao(), this)
    }
}