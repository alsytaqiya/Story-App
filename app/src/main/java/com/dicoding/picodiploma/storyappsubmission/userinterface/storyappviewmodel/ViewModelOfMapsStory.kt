package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory.RepositoryOfMyStory

class ViewModelOfMapsStory(private val storyRepo: RepositoryOfMyStory) : ViewModel() {
    fun getStory(token: String) = storyRepo.getMap(token)
}