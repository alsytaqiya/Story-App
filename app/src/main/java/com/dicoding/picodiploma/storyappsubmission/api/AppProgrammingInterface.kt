package com.dicoding.picodiploma.storyappsubmission.api

import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfStory
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseOfStoryAppRegister
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.ResponseStoryAppLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AppProgrammingInterface {
    @FormUrlEncoded
    @POST("login")
    suspend fun loginToMyStoryApp(
        @Field("email") email: String,
        @Field("password") pass: String
    ): ResponseStoryAppLogin

    @FormUrlEncoded
    @POST("register")
    suspend fun registerToMyStoryApp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ): ResponseOfStoryAppRegister

    @GET("stories")
    suspend fun getMyStoryApp(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ResponseOfStory

    @Multipart
    @POST("stories")
    suspend fun uploadingImageMyStoryApp(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String,
        @Part("lat") latitude: RequestBody?,
        @Part("lon") longitude: RequestBody?,
    ): ResponseOfStoryAppRegister

    @GET("stories?location=1")
    suspend fun getLocationOfStory(
        @Header("Authorization") token: String
    ): ResponseOfStory
}