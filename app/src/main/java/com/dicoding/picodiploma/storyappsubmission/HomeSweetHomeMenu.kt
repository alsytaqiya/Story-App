package com.dicoding.picodiploma.storyappsubmission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.storyappsubmission.data.modeldata.LoginDataClass
import com.dicoding.picodiploma.storyappsubmission.data.storagedata.StoryAccountPreference
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityHomeSweetHomeMenuBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.homesweethome.HomeSweetHome
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelFactoryOfMyStoryApp
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelOfHomeSweetHome
import kotlinx.coroutines.launch

private val Context.dataStoreMyStoryApp: DataStore<Preferences> by preferencesDataStore(name = "akunstory")

class HomeSweetHomeMenu : AppCompatActivity() {
    private lateinit var binding: ActivityHomeSweetHomeMenuBinding
    private lateinit var viewModelOfHomeSweetHome: ViewModelOfHomeSweetHome
    private lateinit var loginDataClass: LoginDataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeSweetHomeMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel()
        button()
    }

    private fun button() {
        binding.locationOfTheStory.setOnClickListener {
            val storyView = Intent(this@HomeSweetHomeMenu, HomeSweetHome::class.java)
            storyView.putExtra(HomeSweetHome.EXTRA_USER, loginDataClass)
            startActivity(storyView)
            finish()
        }
    }

    private fun viewModel() {

        val preferences = StoryAccountPreference.getInstanceStoryApp(dataStoreMyStoryApp)
        viewModelOfHomeSweetHome = ViewModelProvider(
            this, ViewModelFactoryOfMyStoryApp(preferences)
        )[ViewModelOfHomeSweetHome::class.java]
        lifecycleScope.launchWhenCreated {
            launch {
                viewModelOfHomeSweetHome.getUser().collect {
                    loginDataClass = LoginDataClass(
                        it.name,
                        it.email,
                        it.userId,
                        it.token,
                        true
                    )
                }
            }
        }
    }


}