package com.portfolio.todo_mvvm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.portfolio.todo_mvvm.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarDateAdapter(val calendarDates: List<LocalDate>): RecyclerView.Adapter<CalendarDateAdapter.ViewHolder>() {
    var onItemClick: ((LocalDate) -> Unit)? = null

    private var selectedDate: LocalDate? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val itemCalendar: LinearLayoutCompat = view.findViewById(R.id.itemCalendar)
        val textViewMonthDay: AppCompatTextView = view.findViewById(R.id.textViewMonthDay)
        val textViewWeekDay: AppCompatTextView = view.findViewById(R.id.textViewWeekDay)
        val indicator: CardView = view.findViewById(R.id.indicator)

        fun setDate(currentDate: LocalDate) {
            val dayOfWeek = currentDate.format(DateTimeFormatter.ofPattern("E"))
            val dayOfMonth = currentDate.dayOfMonth.toString().padStart(2, '0')

            textViewWeekDay.text = dayOfWeek
            textViewMonthDay.text = dayOfMonth
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = calendarDates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDate = calendarDates[position]
        holder.setDate(currentDate)

        if (currentDate == selectedDate) {
            holder.indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.main))
            holder.textViewMonthDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.main))
            holder.textViewWeekDay.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.main))
        }

        holder.itemCalendar.setOnClickListener {
            Log.d("Saba", "itemCalendar - setOnClickListener")
            onItemClick?.invoke(currentDate)
        }
    }

    fun setSelectedDate(date: LocalDate?) {
        selectedDate = date
        notifyDataSetChanged() // Update the adapter to refresh item views
    }
}