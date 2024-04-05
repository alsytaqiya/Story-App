package com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.storyappsubmission.data.storagedata.StoryAccountPreference
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivitySettingAppUserBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.loginofmyappstory.MainActivity
import com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.preferenceofmine.StoryAppSettingPreference
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelFactoryOfMyStoryApp
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelOfHomeSweetHome

private val Context.dataStoreMyStoryApp: DataStore<Preferences> by preferencesDataStore(name = "storyaccount")

class SettingAppUser : AppCompatActivity() {
    private val prefMyStoryApp by lazy { StoryAppSettingPreference(this) }
    private lateinit var binding: ActivitySettingAppUserBinding
    private lateinit var settingViewModelOfMyStoryApp: ViewModelOfHomeSweetHome


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingAppUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.themeModeToggle.isChecked = prefMyStoryApp.getBoolean(MODE_DARK_THEME)
        binding.themeModeToggle.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            when (isChecked) {
                true -> {
                    prefMyStoryApp.put(MODE_DARK_THEME, true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false -> {
                    prefMyStoryApp.put(MODE_DARK_THEME, false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

            }

        }
        modelStoryView()
        bindingButtonOfMyStoryApp()

    }

    private fun bindingButtonOfMyStoryApp() {

        binding.apply {
            btnLanguages.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            btnLogoutFromApp.setOnClickListener {
                binding.cardViewLogout.visibility = View.VISIBLE
            }

            buttonClickingYes.setOnClickListener {
                settingViewModelOfMyStoryApp.logout()
                loadingSetting(true)
                val intent = Intent(this@SettingAppUser, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            buttonClickingNo.setOnClickListener {
                binding.cardViewLogout.visibility = View.GONE
            }
        }
    }


    private fun modelStoryView() {

        val preferences = StoryAccountPreference.getInstanceStoryApp(dataStoreMyStoryApp)
        settingViewModelOfMyStoryApp = ViewModelProvider(
            this, ViewModelFactoryOfMyStoryApp(preferences)
        )[ViewModelOfHomeSweetHome::class.java]


    }


    private fun loadingSetting(state: Boolean) {
        if (state) {
            binding.progressBarLogout.visibility = View.VISIBLE
        } else {
            binding.progressBarLogout.visibility = View.GONE
        }
    }


    companion object {
        const val MODE_DARK_THEME = "mode_dark_theme"
    }
}