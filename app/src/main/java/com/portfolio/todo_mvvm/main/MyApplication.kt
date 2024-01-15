package com.portfolio.todo_mvvm.main

import android.app.Application
import androidx.room.Room
import com.portfolio.todo_mvvm.db.AppDatabase

class MyApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        // Initialize Room Database
        database = Room.databaseBuilder(this, AppDatabase::class.java, "tasksDb")
            .fallbackToDestructiveMigration()
            .build()
    }
}