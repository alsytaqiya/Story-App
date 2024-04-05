package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory.RepositoryOfMyStory
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp

class ModelOfListItemHome(private val repositoryOfMyStory: RepositoryOfMyStory) : ViewModel() {
    fun getStory(token: String): LiveData<PagingData<MyStoryApp>> {
        return repositoryOfMyStory.getStoryPaging(token).cachedIn(viewModelScope).asLiveData()
    }
}