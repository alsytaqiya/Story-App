package com.dicoding.picodiploma.storyappsubmission.userinterface.onboardingpageofmystoryapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityOnBoardingPageOfMyStoryAppBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.SettingAppUser
import com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.preferenceofmine.StoryAppSettingPreference
import com.dicoding.picodiploma.storyappsubmission.userinterface.splashscreenofmystoryapp.SplashScreenOfMyStoryApp

class OnBoardingPageOfMyStoryApp : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingPageOfMyStoryAppBinding
    private val prefMyStoryApp by lazy { StoryAppSettingPreference(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingPageOfMyStoryAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val view = Intent(this@OnBoardingPageOfMyStoryApp, SplashScreenOfMyStoryApp::class.java)
            startActivity(view)
            finish()
        }

        when (prefMyStoryApp.getBoolean(SettingAppUser.MODE_DARK_THEME)) {
            true -> {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }

    }

}