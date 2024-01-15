package com.portfolio.todo_mvvm.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val isCompleted: Boolean = false,
    val date: java.util.Date,
    val groupId: Long = 0
)

