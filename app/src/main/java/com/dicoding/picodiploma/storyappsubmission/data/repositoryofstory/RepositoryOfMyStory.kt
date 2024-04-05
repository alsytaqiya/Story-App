package com.dicoding.picodiploma.storyappsubmission.data.repositoryofstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.picodiploma.storyappsubmission.api.AppProgrammingInterface
import com.dicoding.picodiploma.storyappsubmission.data.RemoteStoryOfModerator
import com.dicoding.picodiploma.storyappsubmission.data.databaseroom.DatabaseOfStory
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfResult
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfStoryAppRegister
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.StoryAppLogin
import com.dicoding.picodiploma.storyappsubmission.helpme.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RepositoryOfMyStory(
    private val databaseOfStory: DatabaseOfStory,
    private val appProgrammingInterface: AppProgrammingInterface
) {
    fun accountRegistration(
        name: String,
        email: String,
        pass: String
    ):
            LiveData<ResponseOfResult<ResponseOfStoryAppRegister>> = liveData {
        emit(ResponseOfResult.Loading)
        try {
            val response = appProgrammingInterface.registerToMyStoryApp(name, email, pass)
            if (!response.error) {
                emit(ResponseOfResult.Success(response))
            } else {
                Log.e(TAG, "Register Fail: ${response.message}")
                emit(ResponseOfResult.Error(response.message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Register Exception: ${e.message.toString()} ")
            emit(ResponseOfResult.Error(e.message.toString()))
        }
    }

    fun accountLogin(email: String, pass: String): LiveData<ResponseOfResult<StoryAppLogin>> =
        liveData {
            emit(ResponseOfResult.Loading)
            try {
                val loginResponse = appProgrammingInterface.loginToMyStoryApp(email, pass)
                if (!loginResponse.error) {
                    emit(ResponseOfResult.Success(loginResponse.loginResult))
                } else {
                    Log.e(TAG, "Login Fail: ${loginResponse.message}")
                    emit(ResponseOfResult.Error(loginResponse.message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login Exception: ${e.message.toString()} ")
                emit(ResponseOfResult.Error(e.message.toString()))
            }
        }

    fun getMap(token: String): LiveData<ResponseOfResult<List<MyStoryApp>>> =
        liveData {
            emit(ResponseOfResult.Loading)
            try {
                val response = appProgrammingInterface.getLocationOfStory("Bearer $token")
                if (!response.error) {
                    emit(ResponseOfResult.Success(response.listStory))
                } else {
                    Log.e(TAG, "GetMap Fail: ${response.message}")
                    emit(ResponseOfResult.Error(response.message))
                }

            } catch (e: Exception) {
                Log.e(TAG, "GetMap Exception: ${e.message.toString()} ")
                emit(ResponseOfResult.Error(e.message.toString()))
            }
        }

    fun uploadMyLovelyStory(
        token: String, description: RequestBody,
        imageMultipart: MultipartBody.Part, lat: RequestBody? = null,
        lon: RequestBody? = null
    ): LiveData<ResponseOfResult<ResponseOfStoryAppRegister>> = liveData {
        emit(ResponseOfResult.Loading)
        try {
            val response = appProgrammingInterface.uploadingImageMyStoryApp(
                imageMultipart, description, "Bearer $token", lat, lon
            )
            if (!response.error) {
                emit(ResponseOfResult.Success(response))
            } else {
                Log.e(TAG, "uploadMyLovelyStory Fail: ${response.message}")
                emit(ResponseOfResult.Error(response.message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadMyLovelyStory Exception: ${e.message.toString()} ")
            emit(ResponseOfResult.Error(e.message.toString()))
        }
    }

    fun getStoryPaging(token: String): Flow<PagingData<MyStoryApp>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 7
                ),
                remoteMediator = RemoteStoryOfModerator(
                    databaseOfStory,
                    appProgrammingInterface,
                    token
                ),
                pagingSourceFactory = {
                    databaseOfStory.dataAccessObjectStory().getMyStoryApp()
                }
            ).flow
        }
    }


    companion object {
        private const val TAG = "StoryRepository"
    }
}