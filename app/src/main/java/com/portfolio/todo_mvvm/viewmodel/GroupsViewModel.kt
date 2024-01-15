package com.portfolio.todo_mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.db.models.Task
import com.portfolio.todo_mvvm.repository.TasksRepository
import kotlinx.coroutines.launch

class GroupsViewModel(private val repository: TasksRepository) : ViewModel() {
    val groups = liveData {
        emitSource(repository.getGroups())
    }
}
