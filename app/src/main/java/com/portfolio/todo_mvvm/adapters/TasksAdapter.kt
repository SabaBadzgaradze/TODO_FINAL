package com.portfolio.todo_mvvm.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.portfolio.todo_mvvm.R
import com.portfolio.todo_mvvm.db.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TasksAdapter(
    private var tasks: List<Task>
): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    var onItemClick: ((Task) -> Unit)? = null
    var onItemDelete: ((Task) -> Unit)? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val itemTask: CardView = view.findViewById(R.id.itemTask)
        val radioButton: AppCompatRadioButton = view.findViewById(R.id.radioButton)
        val textViewTitle: AppCompatTextView = view.findViewById(R.id.textViewTitle)
        val textViewTask: AppCompatTextView = view.findViewById(R.id.textViewTask)
        val textViewDate: AppCompatTextView = view.findViewById(R.id.textViewDate)
        val imageViewDelete: AppCompatImageView = view.findViewById(R.id.imageViewDelete)

        @SuppressLint("ResourceAsColor")
        fun setData(task: Task) {
            textViewTitle.text = task.title
            textViewTask.text = task.content
            radioButton.isChecked = task.isCompleted

            if (task.isCompleted) {
                imageViewDelete.visibility = View.VISIBLE
                textViewDate.visibility = View.GONE

                textViewTitle.setTextColor(R.color.textSecondary)
            } else {
                imageViewDelete.visibility = View.GONE
                textViewDate.visibility = View.VISIBLE

                // Set date
                val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val timeString = format.format(task.date)

                textViewDate.text = timeString
            }
        }
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]

        holder.setData(task)

        holder.itemTask.setOnClickListener {
            onItemClick?.invoke(task)
        }

        holder.imageViewDelete.setOnClickListener {
            onItemDelete?.invoke(task)
        }

        holder.radioButton.setOnClickListener {
            onItemClick?.invoke(task)
        }
    }
}