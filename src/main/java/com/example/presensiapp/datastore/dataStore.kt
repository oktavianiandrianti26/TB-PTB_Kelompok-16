package com.example.presensiapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.presensiapp.models.User
import com.example.presensiapp.models.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

class DataStoreManager(private val context: Context) {
    companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_HP = stringPreferencesKey("user_hp")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_ADDRESS = stringPreferencesKey("user_address")
        val USER_IMAGE = stringPreferencesKey("user_image")
    }

    // Save user data
    suspend fun saveUser(userData: User) {
        val user = userData as User
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = user.id.toString()
            preferences[USER_HP] = user.hp
            preferences[USER_EMAIL] = user.email
            preferences[USER_PASSWORD] = user.password
            preferences[USER_NAME] = user.name
            preferences[USER_ADDRESS] = user.address
            preferences[USER_IMAGE] = user.image
        }
    }

    // Get user email
    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }
    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID]
    }


    // Get user password
    val userPassword: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_PASSWORD]
    }

    val getUserData: Flow<User?> = context.dataStore.data.map { preferences ->
        val userId = preferences[USER_ID]?.toIntOrNull() ?: 0
        val userName = preferences[USER_NAME] ?: ""
        val userHp = preferences[USER_HP] ?: ""
        val userPassword = preferences[USER_PASSWORD] ?: ""
        val userAddress = preferences[USER_ADDRESS] ?: ""
        val userImage = preferences[USER_IMAGE] ?: ""
        val userEmail = preferences[USER_EMAIL] ?: ""

        if (userName.isNotEmpty() && userEmail.isNotEmpty()) {
            User(
                id = userId,
                name = userName,
                hp = userHp,
                password = userPassword,
                created_at = "",
                address = userAddress,
                image = userImage,
                email = userEmail
            )
        } else {
            null
        }
    }

    suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear() // Clears all data in the DataStore
        }
    }


}
