package com.dicoding.picodiploma.storyappsubmission.userinterface.splashscreenofmystoryapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.storyappsubmission.HomeSweetHomeMenu
import com.dicoding.picodiploma.storyappsubmission.data.storagedata.StoryAccountPreference
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivitySplashScreenOfMyStoryAppBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.loginofmyappstory.MainActivity
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelFactoryOfMyStoryApp
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelOfHomeSweetHome
import kotlinx.coroutines.launch

private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name = "akunstory")

class SplashScreenOfMyStoryApp : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenOfMyStoryAppBinding
    private val second = 2000L
    private lateinit var viewModelOfHomeSweetHome: ViewModelOfHomeSweetHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenOfMyStoryAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModelSetup()
    }

    private fun viewModelSetup() {
        viewModelOfHomeSweetHome = ViewModelProvider(
            this,
            ViewModelFactoryOfMyStoryApp(
                StoryAccountPreference.getInstanceStoryApp(
                    dataStoreStoryApp
                )
            )
        )[ViewModelOfHomeSweetHome::class.java]

        lifecycleScope.launchWhenCreated {
            launch {
                viewModelOfHomeSweetHome.getUser().collect {
                    if (it.isLogin) {
                        mainActivityToGo(true)
                    } else mainActivityToGo(false)
                }
            }
        }
    }

    private fun mainActivityToGo(boolean: Boolean) {
        if (boolean) {
            Handler(Looper.getMainLooper()).postDelayed({

                startActivity(
                    Intent(this, HomeSweetHomeMenu::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle()
                )
                finish()
            }, second)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(
                    Intent(this, MainActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle()
                )
                finish()
            }, second)
        }
    }


}
