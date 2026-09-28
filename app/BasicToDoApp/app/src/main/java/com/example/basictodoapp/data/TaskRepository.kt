package com.example.basictodoapp.data

import com.example.basictodoapp.domain.SortOrder
import com.example.basictodoapp.domain.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(sortOrder: SortOrder): Flow<List<Task>>
    suspend fun addTask(title: String, createdAt: Long, updatedAt: Long)
    suspend fun updateTask(id: Long, title: String, updatedAt: Long)
    suspend fun deleteTask(id: Long)
}
