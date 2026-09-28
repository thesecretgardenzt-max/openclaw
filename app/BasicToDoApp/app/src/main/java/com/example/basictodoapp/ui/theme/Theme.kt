package com.example.basictodoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PinkPrimary = Color(0xFFE91E63)
private val PinkPrimaryContainer = Color(0xFFFFD8E7)
private val PinkOnPrimaryContainer = Color(0xFF3F001C)
private val AppBackground = Color(0xFFFFF8FA)
private val AppSurface = Color(0xFFFFFBFF)
private val AppSurfaceVariant = Color(0xFFF7EEF3)

private val LightColors = lightColorScheme(
    primary = PinkPrimary,
    onPrimary = Color.White,
    primaryContainer = PinkPrimaryContainer,
    onPrimaryContainer = PinkOnPrimaryContainer,
    background = AppBackground,
    surface = AppSurface,
    surfaceVariant = AppSurfaceVariant,
    error = Color(0xFFBA1A1A)
)

private val DarkColors = darkColorScheme(
    primary = PinkPrimary,
    onPrimary = Color.White,
    primaryContainer = PinkOnPrimaryContainer,
    onPrimaryContainer = PinkPrimaryContainer,
    error = Color(0xFFFFB4AB)
)

@Composable
fun BasicToDoAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, typography = Typography(), content = content)
}
