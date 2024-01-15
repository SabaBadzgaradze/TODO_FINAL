package com.portfolio.todo_mvvm.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.portfolio.todo_mvvm.broadcastreceiver.TaskReminderReceiver
import com.portfolio.todo_mvvm.db.models.Task
import com.portfolio.todo_mvvm.repository.TasksRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date
import java.util.concurrent.TimeUnit

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {

    fun getTasks(groupId: Long) = liveData {
        emitSource(repository.getTasksByGroupId(groupId))
    }

    fun getTasksByDate(date: LocalDate) = liveData {
        emitSource(repository.getTasksBySelectedDate(date))
    }

    fun insertTask(context: Context, task: Task, date: Date) {
        Log.d("Saba", "insertTask")
        viewModelScope.launch {
            Log.d("Saba", "insertTask - viewModelScope.launch")
            val taskID = repository.insertTask(task)
            try {
                scheduleAlarm(context, taskID, date.time)
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.e("Saba", it) }
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // MARK: - Alarm
    fun scheduleAlarm(context: Context, taskId: Long, dueDate: Long) {
        Log.d("Saba", "scheduleAlarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra("TASK_ID", taskId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val triggerTime = dueDate - TimeUnit.MINUTES.toMillis(5) // 5 minutes before the task due date
        try {
            Log.d("Saba", "scheduleAlarm - try")
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } catch (e: SecurityException) {
            e.localizedMessage?.let { Log.e("Saba", it) }
        }
    }
}
