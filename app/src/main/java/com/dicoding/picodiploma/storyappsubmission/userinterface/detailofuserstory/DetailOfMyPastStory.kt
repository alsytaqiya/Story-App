package com.dicoding.picodiploma.storyappsubmission.userinterface.detailofuserstory

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityDetailOfMyPastStoryBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine.AddStoryOfMine
import com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.SettingAppUser
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelOfStoryDetail

class DetailOfMyPastStory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOfMyPastStoryBinding

    private lateinit var myStoryApp: MyStoryApp

    private val detailViewModel: ViewModelOfStoryDetail by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOfMyPastStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingButton()
        viewDetailStory()


    }

    private fun bindingButton() {
        binding.btnAddAStory.setOnClickListener {
            val view = Intent(this@DetailOfMyPastStory, AddStoryOfMine::class.java)
            startActivity(view)
        }

        binding.btnSettingDetailStory.setOnClickListener {
            val view = Intent(this@DetailOfMyPastStory, SettingAppUser::class.java)
            startActivity(view)

        }
    }

    private fun viewDetailStory() {
        myStoryApp = intent.getParcelableExtra(STORY)!!
        detailViewModel.detailStory(myStoryApp)

        binding.apply {
            nameOfPersonInDetailStory.text = detailViewModel.itemOfMyAppStory.name
            descriptionDetailStory.text = detailViewModel.itemOfMyAppStory.description
            imageViewDetailStory.load(myStoryApp.photoUrl)
        }

    }

    companion object {
        const val STORY = "story"
    }
}