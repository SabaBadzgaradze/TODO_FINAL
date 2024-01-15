package com.portfolio.todo_mvvm.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.portfolio.todo_mvvm.R
import com.portfolio.todo_mvvm.adapters.TasksAdapter
import com.portfolio.todo_mvvm.databinding.FragmentTasksBinding
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.main.MainActivity
import com.portfolio.todo_mvvm.main.MyApplication
import com.portfolio.todo_mvvm.repository.TasksRepository
import com.portfolio.todo_mvvm.viewmodel.TasksViewModel
import com.portfolio.todo_mvvm.viewmodel.TasksViewModelFactory

class TasksFragment: Fragment(R.layout.fragment_tasks) {
    // MARK: Variables
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentActivity = (activity as? MainActivity)?: return

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager

        tasksAdapter = TasksAdapter(emptyList())

        tasksAdapter.onItemClick = { task ->
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            viewModel.updateTask(updatedTask)
        }

        tasksAdapter.onItemDelete = { task ->
            viewModel.deleteTask(task)
        }

        binding.recyclerView.adapter = tasksAdapter

        arguments?.let {
            val groupId = it.getLong("id")
            val label = it.getString("label")?: "Tasks"

            Log.d("Saba", "label")
            Log.d("Saba", label)
            parentActivity.setAppBarTitle(label)
            parentActivity.activeGroupId = groupId
            parentActivity.menuButtonVisibility(View.VISIBLE)
            parentActivity.propertiesButtonVisibility(View.VISIBLE)

            setupObservers(groupId)
        }

    }

    private fun setupObservers(groupId: Long) {
        try {
            viewModel.getTasks(groupId).observe(viewLifecycleOwner) { tasks ->
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