package com.portfolio.todo_mvvm.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.portfolio.todo_mvvm.databinding.FragmentDeleteGroupBottomSheetBinding
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.main.MyApplication
import com.portfolio.todo_mvvm.repository.TasksRepository
import kotlinx.coroutines.launch

class DeleteGroupBottomSheetFragment: BottomSheetDialogFragment() {
    // MARK: Variables
    private var _binding: FragmentDeleteGroupBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TasksRepository

    var groupId: Long = -1L

    // MARK: Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appDatabase = (requireActivity().application as MyApplication).database
        repository = TasksRepository(appDatabase.groupsDao(), appDatabase.tasksDao())
        groupId = arguments?.getLong("id", -1)?: -1 // -1 as default or handle appropriately
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteGroupBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerListeners()
    }

    private fun registerListeners() {
        binding.buttonDismiss.setOnClickListener {
            dismiss()
        }

        binding.buttonDeleteGroup.setOnClickListener {
            Log.d("Saba", groupId.toString())
            if (groupId == -1L) { return@setOnClickListener }
            Log.d("Saba", groupId.toString())

            try {
                lifecycleScope.launch {
                    repository.deleteGroupById(groupId)
                    repository.deleteTasksByGroupId(groupId)
                    dismiss()
                }
            } catch (e: Exception) {
                e.localizedMessage?.let { it1 -> Log.e("Saba", it1) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}