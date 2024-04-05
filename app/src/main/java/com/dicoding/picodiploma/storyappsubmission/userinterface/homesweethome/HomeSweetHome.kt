package com.dicoding.picodiploma.storyappsubmission.userinterface.homesweethome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.storyappsubmission.data.modeldata.LoginDataClass
import com.dicoding.picodiploma.storyappsubmission.data.storagedata.StoryAccountPreference
import com.dicoding.picodiploma.storyappsubmission.databinding.ActivityHomeSweetHomeBinding
import com.dicoding.picodiploma.storyappsubmission.userinterface.addingstoryofmine.AddStoryOfMine
import com.dicoding.picodiploma.storyappsubmission.userinterface.homesweethome.adapterofmystoryapp.AdapterOfLoading
import com.dicoding.picodiploma.storyappsubmission.userinterface.homesweethome.adapterofmystoryapp.HomeSweetHomeOfAdapter
import com.dicoding.picodiploma.storyappsubmission.userinterface.loginofmyappstory.MainActivity
import com.dicoding.picodiploma.storyappsubmission.userinterface.maps.MapsOfStory
import com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.SettingAppUser
import com.dicoding.picodiploma.storyappsubmission.userinterface.settingofmystoryapp.preferenceofmine.StoryAppSettingPreference
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.FactoryOfViewModel
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ModelOfListItemHome
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelFactoryOfMyStoryApp
import com.dicoding.picodiploma.storyappsubmission.userinterface.storyappviewmodel.ViewModelOfHomeSweetHome
import kotlinx.coroutines.launch

private val Context.dataStoreMyStoryApp: DataStore<Preferences> by preferencesDataStore(name = "akunstory")


class HomeSweetHome : AppCompatActivity() {

    private var binding_: ActivityHomeSweetHomeBinding? = null
    private val binding get() = binding_
    private val prefMyStoryApp by lazy { StoryAppSettingPreference(this) }

    private lateinit var loginDataClass: LoginDataClass
    private lateinit var homeSweetHomeOfAdapter: HomeSweetHomeOfAdapter
    private val modelOfListItemHome: ModelOfListItemHome by viewModels {
        FactoryOfViewModel.getInstance(this)
    }
    private lateinit var viewModelOfHomeSweetHome: ViewModelOfHomeSweetHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ = ActivityHomeSweetHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val preferences = StoryAccountPreference.getInstanceStoryApp(dataStoreMyStoryApp)
        viewModelOfHomeSweetHome = ViewModelProvider(
            this, ViewModelFactoryOfMyStoryApp(preferences)
        )[ViewModelOfHomeSweetHome::class.java]

        if (prefMyStoryApp.getBoolean(SettingAppUser.MODE_DARK_THEME)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        bindingButton()
        recyclerSwipeRefresh()


    }

    override fun onResume() {
        super.onResume()
        recyclerViewInitAdapter()
    }

    private fun bindingButton() {
        binding?.btnAddAStory?.setOnClickListener {
            val view = Intent(this, AddStoryOfMine::class.java)
            view.putExtra(AddStoryOfMine.EXTRA_USER, loginDataClass)
            startActivity(view)
        }

        binding?.btnSettingHome?.setOnClickListener {
            val view = Intent(this@HomeSweetHome, SettingAppUser::class.java)
            startActivity(view)

        }
        binding?.locationOfStory?.setOnClickListener {
            val viewLocation = Intent(this, MapsOfStory::class.java)
            viewLocation.putExtra(MapsOfStory.EXTRA_USER, loginDataClass)
            startActivity(viewLocation)
        }
    }

    private fun recyclerViewInitAdapter() {
        homeSweetHomeOfAdapter = HomeSweetHomeOfAdapter()
        binding?.storyAppRecyclerView?.adapter =
            homeSweetHomeOfAdapter.withLoadStateHeaderAndFooter(
                footer = AdapterOfLoading { homeSweetHomeOfAdapter.retry() },
                header = AdapterOfLoading { homeSweetHomeOfAdapter.retry() }
            )

        binding?.storyAppRecyclerView?.layoutManager = LinearLayoutManager(this)
        binding?.storyAppRecyclerView?.setHasFixedSize(true)

        lifecycleScope.launchWhenCreated {
            homeSweetHomeOfAdapter.loadStateFlow.collect {
                binding?.swipe?.isRefreshing = it.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            launch {
                viewModelOfHomeSweetHome.getUser().collect {
                    if (it.token.isEmpty()) {
                        startActivity(Intent(this@HomeSweetHome, MainActivity::class.java))
                        finish()
                    } else {
                        loginDataClass = LoginDataClass(
                            it.name,
                            it.email,
                            it.userId,
                            it.token,
                            true
                        )
                        modelOfListItemHome.getStory(it.token).observe(this@HomeSweetHome) {
                            homeSweetHomeOfAdapter.submitData(lifecycle, it)

                        }
                    }

                }
            }
        }

    }

    private fun recyclerSwipeRefresh() {
        binding?.swipe?.setOnRefreshListener {
            homeSweetHomeOfAdapter.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding_ = null
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}

