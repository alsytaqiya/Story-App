package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory.RepositoryOfMyStory

class ThisIsTheMainOfViewModel(private val repositoryOfMyStory: RepositoryOfMyStory) :
    ViewModel() {
    fun login(email: String, password: String) =
        repositoryOfMyStory.accountLogin(email, password)
}