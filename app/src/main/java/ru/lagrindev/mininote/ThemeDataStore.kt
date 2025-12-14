package ru.lagrindev.mininote

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")
val DARK_MODE = booleanPreferencesKey("dark_mode")

fun Context.isDarkTheme(): Flow<Boolean> =
    dataStore.data.map { it[DARK_MODE] ?: true }

suspend fun Context.saveTheme(dark: Boolean) {
    dataStore.edit { it[DARK_MODE] = dark }
}
