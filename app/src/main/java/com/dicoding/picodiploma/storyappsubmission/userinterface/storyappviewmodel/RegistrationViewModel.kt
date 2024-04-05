package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory.RepositoryOfMyStory

class RegistrationViewModel(private val repositoryOfMyStory: RepositoryOfMyStory) : ViewModel() {
    fun registerMyStoryAccount(name: String, email: String, password: String) =
        repositoryOfMyStory.accountRegistration(name, email, password)
}
