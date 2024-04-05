package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.storyappsubmission.data.storagedata.StoryAccountPreference

class ViewModelFactoryOfMyStoryApp(private val preferenceOfMyStoryApp: StoryAccountPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViewModelOfHomeSweetHome::class.java) -> {
                ViewModelOfHomeSweetHome(preferenceOfMyStoryApp) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}