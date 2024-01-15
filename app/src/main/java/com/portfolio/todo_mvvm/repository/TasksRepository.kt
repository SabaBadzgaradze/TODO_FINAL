package com.portfolio.todo_mvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.portfolio.todo_mvvm.db.dao.GroupsDao
import com.portfolio.todo_mvvm.db.dao.TasksDao
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.db.models.Task
import java.time.LocalDate
import java.util.Date

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay().toInstant(java.time.ZoneOffset.UTC))
}

class TasksRepository(private val groupsDao: GroupsDao, private val tasksDao: TasksDao) {
    suspend fun insertGroup(group: Group) = groupsDao.insertGroup(group)
    fun getGroups(): LiveData<List<Group>> = groupsDao.getGroups()
    suspend fun deleteGroupById(groupId: Long) { groupsDao.deleteGroupById(groupId) }

    suspend fun insertTask(task: Task) = tasksDao.insertTask(task)
    fun getTasksByGroupId(groupId: Long): LiveData<List<Task>> = tasksDao.getTasksByGroupId(groupId)
    suspend fun updateTask(task: Task) { tasksDao.updateTask(task) }
    suspend fun deleteTask(task: Task) { tasksDao.deleteTask(task) }
    suspend fun deleteTasksByGroupId(groupId: Long) { tasksDao.deleteTasksByGroupId(groupId) }
    fun getTasksBySelectedDate(selectedDate: LocalDate): LiveData<List<Task>> {
        val javaUtilDate = selectedDate.toDate()
        Log.d("Saba", "nutu")
        Log.d("Saba", javaUtilDate.toString())
        Log.d("Saba", tasksDao.getTasksByDate(javaUtilDate).toString())
        return tasksDao.getTasksByDate(javaUtilDate)
    }
}
