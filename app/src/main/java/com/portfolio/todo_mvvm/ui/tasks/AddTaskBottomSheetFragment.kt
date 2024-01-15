package com.portfolio.todo_mvvm.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.portfolio.todo_mvvm.databinding.FragmentAddTaskBottomSheetBinding
import com.portfolio.todo_mvvm.db.models.Task
import com.portfolio.todo_mvvm.main.MainActivity
import com.portfolio.todo_mvvm.main.MyApplication
import com.portfolio.todo_mvvm.repository.TasksRepository
import com.portfolio.todo_mvvm.viewmodel.TasksViewModel
import com.portfolio.todo_mvvm.viewmodel.TasksViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskBottomSheetFragment: BottomSheetDialogFragment() {
    private val viewModel: TasksViewModel by viewModels {
        val appDatabase = (requireActivity().application as MyApplication).database
        val repository = TasksRepository(appDatabase.groupsDao(), appDatabase.tasksDao())
        TasksViewModelFactory(repository)
    }
    // MARK: Variables
    private var _binding: FragmentAddTaskBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var groupId: Long = 0L

    private var taskDueDate: Date? = null

    // MARK: - Private methods
    private fun setupCreateTaskButton() {
        Log.d("Saba", "setupCreateTaskButton")
        binding.buttonCreateTask.setOnClickListener {
            if (taskDueDate == null) {
                Toast.makeText(activity, "Please enter date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("Saba", "setOnClickListener")
            Log.d("Saba", taskDueDate.toString())

            val name = binding.editTextName.text.toString()
            val note = binding.editTextNote.text.toString()
            val newTask = Task(
                title = name,
                content = note,
                date = taskDueDate!!,
                groupId = groupId
            )

            viewModel.insertTask(requireContext(), newTask, taskDueDate!!)
            dismiss()
        }
    }

    private fun registerListeners() {
        binding.buttonDatePicker.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker().setTitleText("Select due date")
        val datePicker = datePickerBuilder.build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            showTimePicker(selectedDate)
        }

        datePicker.show(childFragmentManager, datePicker.tag)
    }

    private fun showTimePicker(date: Date) {
        val timePickerBuilder = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Select due time")
        val timePicker = timePickerBuilder.build()

        timePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
            }
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val dateTimeString = dateTimeFormat.format(calendar.time)
            taskDueDate = calendar.time
            binding.textViewDue.visibility = View.VISIBLE
            binding.textViewDue.text = "Task Due: $dateTimeString"
        }

        timePicker.show(childFragmentManager, timePicker.tag)
    }

    // MARK: Lifecycle Methods
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Saba", "onViewCreated")
        groupId = (activity as MainActivity).activeGroupId
        Log.d("Saba", "onViewCreated - 1")
        setupCreateTaskButton()
        registerListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}