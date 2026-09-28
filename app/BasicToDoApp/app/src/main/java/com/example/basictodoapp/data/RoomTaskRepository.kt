package com.example.basictodoapp.data

import com.example.basictodoapp.domain.SortOrder
import com.example.basictodoapp.domain.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTaskRepository(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun observeTasks(sortOrder: SortOrder): Flow<List<Task>> = when (sortOrder) {
        SortOrder.NEWEST_FIRST -> taskDao.observeNewestFirst()
        SortOrder.OLDEST_FIRST -> taskDao.observeOldestFirst()
    }.map { entities -> entities.map { it.toDomain() } }

    override suspend fun addTask(title: String, createdAt: Long, updatedAt: Long) {
        taskDao.insert(TaskEntity(title = title, createdAt = createdAt, updatedAt = updatedAt))
    }

    override suspend fun updateTask(id: Long, title: String, updatedAt: Long) {
        taskDao.updateTitle(id = id, title = title, updatedAt = updatedAt)
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteById(id = id)
    }
}
