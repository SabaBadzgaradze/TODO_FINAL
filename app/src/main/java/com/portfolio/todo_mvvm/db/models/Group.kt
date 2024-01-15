package com.portfolio.todo_mvvm.db.models

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String = "",
    @DrawableRes val icon: Int = 0,
    val itemCount: Int = 0
)
