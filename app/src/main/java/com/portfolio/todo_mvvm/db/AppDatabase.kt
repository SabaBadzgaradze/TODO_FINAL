package com.portfolio.todo_mvvm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.portfolio.todo_mvvm.db.dao.GroupsDao
import com.portfolio.todo_mvvm.db.dao.TasksDao
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.db.models.Task

@Database(entities = [Group::class, Task::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupsDao(): GroupsDao
    abstract fun tasksDao(): TasksDao
}
