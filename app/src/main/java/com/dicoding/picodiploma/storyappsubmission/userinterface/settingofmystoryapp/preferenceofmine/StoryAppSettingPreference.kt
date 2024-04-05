package com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.preferenceofmine

import android.content.Context
import android.content.SharedPreferences

class StoryAppSettingPreference(prefContext: Context) {
    private val nameMyStoryApp = "MyStoryApp"
    private var prefMyStoryApp: SharedPreferences =
        prefContext.getSharedPreferences(nameMyStoryApp, Context.MODE_PRIVATE)
    private val prefEditMyStoryApp: SharedPreferences.Editor = prefMyStoryApp.edit()

    fun put(key: String, value: Boolean) {
        prefEditMyStoryApp.putBoolean(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
        return prefMyStoryApp.getBoolean(key, false)
    }
}