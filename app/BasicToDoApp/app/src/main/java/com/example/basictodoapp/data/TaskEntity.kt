package com.example.basictodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.basictodoapp.domain.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomain(): Task = Task(
        id = id,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt
)
