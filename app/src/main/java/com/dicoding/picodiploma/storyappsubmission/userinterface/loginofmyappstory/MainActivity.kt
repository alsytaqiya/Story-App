package com.dicoding.picodiploma.storyappsubmission.userinterface.loginofmyappstory

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.storyappsubmission.HomeSweetHomeMenu
import com.dicoding.picodiploma.storyappsubmission.R
import com.dicoding.picodiploma.storyappsubmission.data.modeldata.LoginDataClass
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfResult
import com.dicoding.picodiploma.storyappsubmission.data.storagedata.StoryAccountPreference
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityMainBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.registerofmystoryapp.AccountRegistration
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.FactoryOfViewModel
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ThisIsTheMainOfViewModel

private val Context.dataStoreMyStoryApp: DataStore<Preferences> by preferencesDataStore(name = "akunStory")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val thisIsTheMainOfViewModel: ThisIsTheMainOfViewModel by viewModels {
        FactoryOfViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingButtonOfMyStoryApp()
        loginToAnAccount()
        animationOfLogin()
        supportActionBar?.hide()
    }

    private fun animationOfLogin() {
        ObjectAnimator.ofFloat(binding.ChibiMarukoChan2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun bindingButtonOfMyStoryApp() {
        binding.registrationAccount.setOnClickListener {
            val view = Intent(this@MainActivity, AccountRegistration::class.java)
            startActivity(view)

        }

    }

    private fun loginToAnAccount() {
        binding.loginButton.setOnClickListener {
            val emailOfMyStoryAccount = binding.fillEmail.text.toString()
            val passwordOfMyStoryAccount = binding.fillPassword.text.toString().trim()

            thisIsTheMainOfViewModel.login(emailOfMyStoryAccount, passwordOfMyStoryAccount)
                .observe(this) {
                    when (it) {
                        is ResponseOfResult.Loading -> {
                            binding.progressBarLogin.visibility = View.VISIBLE
                        }
                        is ResponseOfResult.Success -> {
                            binding.progressBarLogin.visibility = View.GONE
                            val login = LoginDataClass(
                                it.data.name,
                                emailOfMyStoryAccount,
                                it.data.userId,
                                it.data.token,
                                true
                            )
                            Toast.makeText(
                                applicationContext,
                                getString((R.string.success_login)),
                                Toast.LENGTH_SHORT
                            ).show()
                            val intentStory = Intent(this, HomeSweetHomeMenu::class.java)
                            intentStory.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intentStory)
                            finish()

                            val storyPreference =
                                StoryAccountPreference.getInstanceStoryApp(dataStoreMyStoryApp)
                            lifecycleScope.launchWhenCreated {
                                storyPreference.saveMyStoryApp(login)
                            }
                        }
                        is ResponseOfResult.Error -> {
                            binding.progressBarLogin.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.inappropriate_username_and_password),
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                }

        }

    }
}