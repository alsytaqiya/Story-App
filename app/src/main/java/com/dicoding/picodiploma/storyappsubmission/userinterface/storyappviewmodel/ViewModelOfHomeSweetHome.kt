package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.storyappsubmission.data.modeldata.LoginDataClass
import com.dicoding.picodiploma.storyappsubmission.data.storagedata.StoryAccountPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ViewModelOfHomeSweetHome(private val StoryAccountPreference: StoryAccountPreference) :
    ViewModel() {

    fun getUser(): Flow<LoginDataClass> {
        return StoryAccountPreference.getMyStoryAccount()
    }

    fun logout() {
        viewModelScope.launch {
            StoryAccountPreference.logoutMyStoryApp()
        }
    }
}