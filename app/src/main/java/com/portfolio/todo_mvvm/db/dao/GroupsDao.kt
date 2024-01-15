package com.portfolio.todo_mvvm.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.portfolio.todo_mvvm.db.models.Group
import com.portfolio.todo_mvvm.db.models.Task

@Dao
interface GroupsDao {
    @Insert
    suspend fun insertGroup(group: Group): Long

    @Query("SELECT * FROM groups")
    fun getGroups(): LiveData<List<Group>>

    @Query("DELETE FROM groups WHERE id = :groupId")
    suspend fun deleteGroupById(groupId: Long)
}
