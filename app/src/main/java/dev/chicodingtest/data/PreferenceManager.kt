package dev.chicodingtest.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.chicodingtest.util.SortOrder
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferenceManager(private val datastore: DataStore<Preferences>) {

    val preferencesFlow = datastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferenceKeys.SORT_ORDER] ?: SortOrder.BY_DEFAULT.name
            )
            sortOrder
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        datastore.edit { preferences ->
            preferences[PreferenceKeys.SORT_ORDER] = sortOrder.name
        }
    }

    private object PreferenceKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    companion object {
        private const val TAG = "PreferenceManager"
    }
}