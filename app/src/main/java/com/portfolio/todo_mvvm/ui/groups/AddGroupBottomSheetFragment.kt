package com.portfolio.todo_mvvm.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.portfolio.todo_mvvm.R
import com.portfolio.todo_mvvm.databinding.FragmentAddGroupBottomSheetBinding
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.main.MyApplication
import com.portfolio.todo_mvvm.repository.TasksRepository
import kotlinx.coroutines.launch

class AddGroupBottomSheetFragment: BottomSheetDialogFragment() {
    // MARK: Variables
    private var _binding: FragmentAddGroupBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TasksRepository

    // MARK: Lifecycle Methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appDatabase = (requireActivity().application as MyApplication).database
        repository = TasksRepository(appDatabase.groupsDao(), appDatabase.tasksDao())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCreateGroup.setOnClickListener {
            val groupName = binding.editTextName.text.toString().trim()
            if (groupName.isNotEmpty()) {
                val newGroup = Group(label = groupName, icon = R.drawable.ic_checked)
                saveGroup(newGroup)
            } else {
                Toast.makeText(activity, "Please enter group name.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveGroup(group: Group) {
        lifecycleScope.launch {
            try {
                repository.insertGroup(group)
                dismiss()
            } catch (e: Exception) {
                Log.e("Saba", "Error saving group", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}