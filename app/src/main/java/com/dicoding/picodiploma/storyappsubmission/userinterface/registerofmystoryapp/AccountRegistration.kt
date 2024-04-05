package com.dicoding.picodiploma.storyappsubmission.userinterface.registerofmystoryapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.storyappsubmission.R
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfResult
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityAccountRegistrationBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.loginofmyappstory.MainActivity
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.FactoryOfViewModel
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.RegistrationViewModel

class AccountRegistration : AppCompatActivity() {
    private lateinit var binding: ActivityAccountRegistrationBinding

    private val registrationViewModel: RegistrationViewModel by viewModels {
        FactoryOfViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurationBinding()

        animationOfRegister()
        supportActionBar?.hide()
    }

    private fun animationOfRegister() {
        ObjectAnimator.ofFloat(binding.ChibiMarukoChan1, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun configurationBinding() {

        binding.buttonRegistrationAccount.setOnClickListener {

            val emailOfMyStoryAppAccount = binding.nameOfUserInRegistration.text.toString()
            val nameOfMyStoryAppAccount = binding.editUserName.text.toString()
            val passwordOfMyStoryAppAccount = binding.userPassword.text.toString()


            when {

                nameOfMyStoryAppAccount.isEmpty() -> {
                    binding.editUserName.error = getString(R.string.filling_name)
                }
                emailOfMyStoryAppAccount.isEmpty() -> {
                    binding.nameOfUserInRegistration.error = getString(R.string.filling_email)
                }
                passwordOfMyStoryAppAccount.isEmpty() -> {
                    binding.userPassword.error = getString(R.string.filling_password)
                }
                else -> {

                    registrationOfMyStoryAppAccount()


                }

            }

        }


    }


    private fun registrationOfMyStoryAppAccount() {
        val emailOfMyStoryAppAccount = binding.nameOfUserInRegistration.text.toString().trim()
        val nameOfMyStoryAppAccount = binding.editUserName.text.toString().trim()
        val passwordOfMyStoryAppAccount = binding.userPassword.text.toString().trim()

        registrationViewModel.registerMyStoryAccount(
            nameOfMyStoryAppAccount,
            emailOfMyStoryAppAccount,
            passwordOfMyStoryAppAccount
        ).observe(this) {
            when (it) {
                is ResponseOfResult.Loading -> {
                    binding.progressBarRegister.visibility = View.VISIBLE
                }
                is ResponseOfResult.Success -> {
                    binding.progressBarRegister.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.successfully_created_account),
                        Toast.LENGTH_LONG
                    ).show()
                    val registrationIntentAccount =
                        Intent(this@AccountRegistration, MainActivity::class.java)
                    registrationIntentAccount.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(registrationIntentAccount)
                    finish()
                }
                is ResponseOfResult.Error -> {
                    binding.progressBarRegister.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.invalid_account),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

}