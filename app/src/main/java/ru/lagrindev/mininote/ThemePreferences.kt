package ru.lagrindev.mininote

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Переименовал, чтобы не было конфликта с другими DataStore
private val Context.themeDataStore by preferencesDataStore("settings")

class ThemePreferences(private val context: Context) {

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    // Flow для чтения текущей темы
    val darkThemeFlow: Flow<Boolean> = context.themeDataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: true // по умолчанию темная тема
        }

    // Сохранение выбранной темы
    suspend fun saveDarkTheme(isDark: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }
}
