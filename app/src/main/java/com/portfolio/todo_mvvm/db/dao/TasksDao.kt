package com.portfolio.todo_mvvm.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.portfolio.todo_mvvm.db.models.Task

@Dao
interface TasksDao {
    @Insert
    suspend fun insertTask(task: Task): Long

    @Query("SELECT * FROM tasks WHERE groupId = :groupId")
    fun getTasksByGroupId(groupId: Long): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE groupId = :groupId")
    suspend fun deleteTasksByGroupId(groupId: Long)

    @Query("SELECT * FROM tasks WHERE date = :selectedDate")
    fun getTasksByDate(selectedDate: java.util.Date): LiveData<List<Task>>

}
