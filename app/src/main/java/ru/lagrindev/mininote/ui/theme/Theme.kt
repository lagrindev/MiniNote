package ru.lagrindev.mininote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/* =======================
   Цвета приложения
   ======================= */

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9FC9FF),
    onPrimary = Color(0xFF003355),

    background = Color(0xFF0F0F0F),
    onBackground = Color.White,

    surface = Color(0xFF1A1A1A),
    onSurface = Color.White,

    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFCCCCCC),

    primaryContainer = Color(0xFF1E3A5F),
    onPrimaryContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0066CC),
    onPrimary = Color.White,

    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,

    surfaceVariant = Color(0xFFEDEDED),
    onSurfaceVariant = Color(0xFF444444),

    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = Color(0xFF002F5F)
)

/* =======================
   Тема приложения
   ======================= */

@Composable
fun MiniNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
