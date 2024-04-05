package com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory.RepositoryOfMyStory
import com.dicoding.picodiploma.storyappsubmission.injection.Injection

class FactoryOfViewModel private constructor(private val repositoryOfMyStory: RepositoryOfMyStory) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ModelOfListItemHome::class.java) -> {
                ModelOfListItemHome(repositoryOfMyStory) as T
            }
            modelClass.isAssignableFrom(ViewModelOfMapsStory::class.java) -> {
                ViewModelOfMapsStory(repositoryOfMyStory) as T
            }
            modelClass.isAssignableFrom(ViewModelOfAddStory::class.java) -> {
                ViewModelOfAddStory(repositoryOfMyStory) as T
            }
            modelClass.isAssignableFrom(ThisIsTheMainOfViewModel::class.java) -> {
                ThisIsTheMainOfViewModel(repositoryOfMyStory) as T
            }
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) -> {
                RegistrationViewModel(repositoryOfMyStory) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

        }
    }

    companion object {
        @Volatile
        private var instance: FactoryOfViewModel? = null
        fun getInstance(context: Context): FactoryOfViewModel =
            instance ?: synchronized(this) {
                instance ?: FactoryOfViewModel(Injection.repositoryProvided(context))
            }.also { instance = it }
    }
}