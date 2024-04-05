package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp

class ViewModelOfStoryDetail : ViewModel() {
    lateinit var itemOfMyAppStory: MyStoryApp

    fun detailStory(myStoryApp: MyStoryApp): MyStoryApp {
        itemOfMyAppStory = myStoryApp
        return itemOfMyAppStory
    }
}