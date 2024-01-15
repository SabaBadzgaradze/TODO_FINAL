package com.portfolio.todo_mvvm.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.portfolio.todo_mvvm.adapters.CalendarDateAdapter
import com.portfolio.todo_mvvm.adapters.TasksAdapter
import com.portfolio.todo_mvvm.databinding.FragmentCalendarBinding
import com.portfolio.todo_mvvm.main.MainActivity
import com.portfolio.todo_mvvm.main.MyApplication
import com.portfolio.todo_mvvm.repository.TasksRepository
import com.portfolio.todo_mvvm.viewmodel.TasksViewModel
import com.portfolio.todo_mvvm.viewmodel.TasksViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarFragment: Fragment() {
    // MARK: Variables
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: LocalDate? = null

    private val viewModel: TasksViewModel by viewModels {
        val appDatabase = (requireActivity().application as MyApplication).database
        val repository = TasksRepository(appDatabase.groupsDao(), appDatabase.tasksDao())
        TasksViewModelFactory(repository)
    }

    private lateinit var tasksAdapter: TasksAdapter

    // MARK: Lifecycle Methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupTasksAdapter()
    }

    // MARK: - Private methods
    private fun init() {
        val parentActivity = (activity as? MainActivity)?: return
        parentActivity.menuButtonVisibility(View.GONE)
        parentActivity.propertiesButtonVisibility(View.VISIBLE)

        // Get current date
        val currentDate = LocalDate.now()

        val currentMonthYear = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        parentActivity.setAppBarTitle(currentMonthYear)

        setupAdapter(currentDate)
    }

    private fun setupAdapter(currentDate: LocalDate) {
        val dateList = generateDateList(currentDate)

        val adapter = CalendarDateAdapter(dateList)
        binding.recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Set the click listener for item selection
        adapter.onItemClick = { calendarDate ->
            val dayOfWeek = calendarDate.dayOfWeek.toString().toLowerCase().capitalize()
            val dayOfMonth = calendarDate.dayOfMonth.toString().padStart(2, '0')
            binding.textViewSelectedDate.text = "$dayOfWeek $dayOfMonth"

            selectedDate = LocalDate.of(currentDate.year, currentDate.month,
                calendarDate.dayOfMonth
            )
            adapter.notifyDataSetChanged() // Update the adapter to refresh item views
        }

        // Pass the selected date to the adapter
        adapter.setSelectedDate(selectedDate)

        binding.recyclerView.adapter = adapter
    }

    private fun generateDateList(currentDate: LocalDate): List<LocalDate> {
        val dateList = mutableListOf<LocalDate>()

        val startDate = currentDate.withDayOfMonth(1)
        val endDate = currentDate.withDayOfMonth(startDate.lengthOfMonth())

        var cDate = startDate
        while (!cDate.isAfter(endDate)) {
            dateList.add(cDate)
            cDate = cDate.plusDays(1)
        }

        return dateList
    }

    private fun setupTasksAdapter() {
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewTasks.layoutManager = layoutManager

        tasksAdapter = TasksAdapter(emptyList())

        tasksAdapter.onItemClick = { task ->
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            viewModel.updateTask(updatedTask)
        }

        tasksAdapter.onItemDelete = { task ->
            viewModel.deleteTask(task)
        }

        binding.recyclerViewTasks.adapter = tasksAdapter

        setupObservers()
    }

    private fun setupObservers() {
        val date = selectedDate ?: return

        try {
            viewModel.getTasksByDate(date).observe(viewLifecycleOwner) { tasks ->
                tasksAdapter.updateTasks(tasks)
            }
        } catch (e: Exception) {
            Log.e("Saba", e.message.toString())
            e.localizedMessage?.let { Log.e("Saba", it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}