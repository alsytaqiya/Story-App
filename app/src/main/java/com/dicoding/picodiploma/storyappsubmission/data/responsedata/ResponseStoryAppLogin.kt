package com.dicoding.picodiploma.storyappsubmission.data.responsedata

data class ResponseStoryAppLogin(

    val error: Boolean,
    val message: String,
    val loginResult: StoryAppLogin
)

data class StoryAppLogin(

    val userId: String,
    val name: String,
    val token: String,

    )
