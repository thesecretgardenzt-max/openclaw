package com.example.basictodoapp.domain

object TaskTitleValidator {
    const val REQUIRED_ERROR = "Task title is required."

    fun normalizeOrNull(rawTitle: String): String? {
        val trimmed = rawTitle.trim()
        return trimmed.ifEmpty { null }
    }
}
