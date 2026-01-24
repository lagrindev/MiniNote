package ru.lagrindev.mininote.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// ✅ Новые основные цвета
val Orange = Color(0xFFFF6202)      // #ff6202 - яркий оранжевый (акценты)
val DarkBlue = Color(0xFF181C21)    // #181c21 - тёмно-синий (фон/текст)
val LightGray = Color(0xFFF2F2F2)   // #f2f2f2 - светло-серый (поверхности)

// Удаляем light схему - только тёмная!

val MiniNoteDarkColorScheme = darkColorScheme(
    // ✅ Primary - оранжевый для кнопок/FAB/акцентов
    primary = Orange,
    onPrimary = Color.White,           // Белый текст на оранжевом

    // ✅ Secondary - светло-серый градиент
    secondary = LightGray,
    onSecondary = DarkBlue,            // Тёмный текст на светлом

    // ✅ Фоны на основе тёмно-синего
    background = DarkBlue,             // Главный фон
    surface = Color(0xFF21262B),       // Карточки (чуть светлее DarkBlue)
    surfaceContainerHigh = Color(0xFF2A3036), // TopBar/BottomBar

    // ✅ Текст
    onSurface = LightGray,             // Основной текст
    onSurfaceVariant = Color(0xFFB0B7C0), // Вторичный текст

    // ✅ Акценты
    tertiary = Orange,                 // Дублируем оранжевый для FAB/успеха
    onTertiary = Color.White,
    error = Color(0xFFFF8A80),         // Красный для ошибок
    onError = Color.White
)
