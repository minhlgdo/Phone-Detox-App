package com.minhlgdo.phonedetoxapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/*
A class to manages whether the service is running or not using DataStore
 */

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("service_prefs")

class ServiceManager @Inject constructor(val context: Context) {

    val getIsRunning: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[isRunning] ?: false
        }

    suspend fun setIsRunning(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isRunning] = value
        }
    }

    companion object {
        private val isRunning = booleanPreferencesKey("service_running")
    }
}