package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory.RepositoryOfMyStory
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ViewModelOfAddStory(private val repositoryOfMyStory: RepositoryOfMyStory) : ViewModel() {
    fun uploadMyStory(
        token: String,
        description: RequestBody,
        image: MultipartBody.Part,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = repositoryOfMyStory.uploadMyLovelyStory(token, description, image, lat, lon)
}