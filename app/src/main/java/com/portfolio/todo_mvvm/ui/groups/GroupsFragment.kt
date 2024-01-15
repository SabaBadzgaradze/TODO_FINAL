package com.portfolio.todo_mvvm.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.portfolio.todo_mvvm.R
import com.portfolio.todo_mvvm.adapters.GroupsAdapter
import com.portfolio.todo_mvvm.databinding.FragmentGroupsBinding
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.main.MainActivity
import com.portfolio.todo_mvvm.main.MyApplication
import com.portfolio.todo_mvvm.repository.TasksRepository
import com.portfolio.todo_mvvm.utils.GridSpacingItemDecoration
import com.portfolio.todo_mvvm.viewmodel.GroupsViewModel
import com.portfolio.todo_mvvm.viewmodel.GroupsViewModelFactory
import kotlinx.coroutines.launch

class GroupsFragment: Fragment() {
    // MARK: Variables
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupsViewModel by viewModels {
        val appDatabase = (requireActivity().application as MyApplication).database
        val repository = TasksRepository(appDatabase.groupsDao(), appDatabase.tasksDao())
        GroupsViewModelFactory(repository)
    }

    private lateinit var groupsAdapter: GroupsAdapter

    // MARK: Lifecycle Methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentActivity = (requireActivity() as? MainActivity)?: return
        parentActivity.setAppBarTitle("My Lists")
        parentActivity.menuButtonVisibility(View.GONE)
        parentActivity.propertiesButtonVisibility(View.VISIBLE)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = gridLayoutManager

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, false))

        groupsAdapter = GroupsAdapter(emptyList())

        groupsAdapter.onItemClick = { group ->
            if (group.id == 0L) {
                val bottomSheetFragment = AddGroupBottomSheetFragment()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            } else {
                val args = Bundle().apply {
                    putLong("id", group.id)
                    putString("label", group.label)
                }

                findNavController().navigate(R.id.action_groupsFragment_to_tasksFragment, args)
            }
        }

        groupsAdapter.onItemLongClick = { group ->
            val bottomSheetFragment = DeleteGroupBottomSheetFragment()

            val args = Bundle()
            args.putLong("id", group.id)

            bottomSheetFragment.arguments = args
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }

        binding.recyclerView.adapter = groupsAdapter

        setupObservers()
    }

    private fun setupObservers() {
        try {
            viewModel.groups.observe(viewLifecycleOwner) { groups ->
                val newGroups = groups.toMutableList()
                newGroups.add(0, Group(0L, "Add List", R.drawable.ic_checked, 0))
                groupsAdapter.updateGroups(newGroups)
                Log.d("Saba", "GroupsFragment - observe groups")
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