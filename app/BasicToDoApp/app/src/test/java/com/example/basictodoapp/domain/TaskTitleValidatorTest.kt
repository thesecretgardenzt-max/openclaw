package com.example.basictodoapp.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TaskTitleValidatorTest {
    @Test
    fun normalizeOrNullRejectsEmptyAndWhitespaceTitles() {
        assertNull(TaskTitleValidator.normalizeOrNull(""))
        assertNull(TaskTitleValidator.normalizeOrNull("     "))
    }

    @Test
    fun normalizeOrNullTrimsValidTitle() {
        assertEquals("Buy milk", TaskTitleValidator.normalizeOrNull("  Buy milk  "))
    }
}
