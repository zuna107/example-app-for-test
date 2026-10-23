package com.mysima.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Zinc50,
    onPrimary = Zinc950,
    background = Zinc950,
    onBackground = Zinc50,
    surface = Color(0xFF121215),
    onSurface = Zinc50,
    surfaceVariant = Zinc900,
    onSurfaceVariant = Zinc400,
    outline = Zinc800
)

private val LightColorScheme = lightColorScheme(
    primary = Zinc950,
    onPrimary = Zinc50,
    background = Color.White,
    onBackground = Zinc950,
    surface = Color.White,
    onSurface = Zinc950,
    surfaceVariant = Zinc100,
    onSurfaceVariant = Zinc500,
    outline = Zinc200
)

@Composable
fun MySIMATheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
