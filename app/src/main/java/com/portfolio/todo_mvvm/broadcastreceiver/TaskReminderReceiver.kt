package com.portfolio.todo_mvvm.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.portfolio.todo_mvvm.R

class TaskReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("TASK_ID", -1)
        Log.d("Saba", "onReceive")
        Log.d("Saba", taskId.toString())
        if (taskId != -1L) {
            showTaskReminderNotification(context, taskId.toInt())
        }
    }

    private fun showTaskReminderNotification(context: Context, taskId: Int) {
        Log.d("Saba", "showTaskReminderNotification")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for API 26 and above
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Task Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Create the notification
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Task Reminder")
            .setContentText("Your task is due in 5 minutes.")
            .setSmallIcon(R.drawable.ic_checked) // Replace with your notification icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Show the notification
        Log.d("Saba", "showTaskReminderNotification - notificationManager.notify(taskId, notification)")
        try {
            notificationManager.notify(taskId, notification)
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.e("Saba", it) }
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "task_reminder_channel"
    }
}
