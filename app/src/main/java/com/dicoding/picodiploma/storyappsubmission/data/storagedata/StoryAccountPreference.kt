package com.dicoding.picodiploma.storyappsubmission.data.storagedata

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dicoding.picodiploma.storyappsubmission.data.modeldata.LoginDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryAccountPreference(private val dataStoreMyStoryApp: DataStore<Preferences>) {
    fun getMyStoryAccount(): Flow<LoginDataClass> {
        return dataStoreMyStoryApp.data.map { preferences ->
            LoginDataClass(
                preferences[ID_KEY_OF_ACCOUNT] ?: "",
                preferences[ACCOUNT_EMAIL_KEY] ?: "",
                preferences[NAME_KEY_OF_ACCOUNT] ?: "",
                preferences[TOKEN_KEY_OF_ACCOUNT] ?: "",
                preferences[STATE_KEY_OF_ACCOUNT] ?: false
            )
        }
    }

    suspend fun saveMyStoryApp(loginStory: LoginDataClass) {
        dataStoreMyStoryApp.edit { preferences ->
            preferences[ID_KEY_OF_ACCOUNT] = loginStory.userId
            preferences[ACCOUNT_EMAIL_KEY] = loginStory.email
            preferences[NAME_KEY_OF_ACCOUNT] = loginStory.name
            preferences[TOKEN_KEY_OF_ACCOUNT] = loginStory.token
            preferences[STATE_KEY_OF_ACCOUNT] = loginStory.isLogin

        }
    }

    suspend fun logoutMyStoryApp() {
        dataStoreMyStoryApp.edit { preferences ->
            preferences.clear()
        }
    }


    companion object {
        @Volatile
        private var STORYAPPINSTANCE: StoryAccountPreference? = null

        private val ID_KEY_OF_ACCOUNT = stringPreferencesKey("userid")
        private val ACCOUNT_EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY_OF_ACCOUNT = stringPreferencesKey("name")
        private val TOKEN_KEY_OF_ACCOUNT = stringPreferencesKey("token")
        private val STATE_KEY_OF_ACCOUNT = booleanPreferencesKey("state")

        fun getInstanceStoryApp(dataStoreMyStoryApp: DataStore<Preferences>): StoryAccountPreference {
            return STORYAPPINSTANCE ?: synchronized(this) {
                val instanceStoryApp = StoryAccountPreference(dataStoreMyStoryApp)
                STORYAPPINSTANCE = instanceStoryApp
                instanceStoryApp
            }
        }
    }

}



