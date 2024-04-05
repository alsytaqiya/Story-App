package com.dicoding.picodiploma.storyappsubmission.injection

import android.content.Context
import com.dicoding.picodiploma.storyappsubmission.api.RetrofitClient
import com.dicoding.picodiploma.storyappsubmission.data.databaseroom.DatabaseOfStory
import com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory.RepositoryOfMyStory

object Injection {
    fun repositoryProvided(context: Context): RepositoryOfMyStory {
        val databaseOfStory = DatabaseOfStory.getInstance(context)
        val retrofitClient = RetrofitClient.getApiMyStoryApp()
        return RepositoryOfMyStory(databaseOfStory, retrofitClient)
    }
}