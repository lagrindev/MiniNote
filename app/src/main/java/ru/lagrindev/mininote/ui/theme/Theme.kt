package ru.lagrindev.mininote.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun MiniNoteTheme(
    content: @Composable () -> Unit
) {
    // ✅ Всегда тёмная тема с новыми 3 цветами
    MaterialTheme(
        colorScheme = MiniNoteDarkColorScheme,
        typography = Typography(),
        content = content
    )
}
