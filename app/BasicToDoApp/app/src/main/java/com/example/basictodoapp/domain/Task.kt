package com.example.basictodoapp.domain

data class Task(
    val id: Long,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long
)
