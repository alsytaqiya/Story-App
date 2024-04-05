package com.dicoding.picodiploma.storyappsubmission.data.responsedata

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class ResponseOfStory(
    val error: Boolean,
    val message: String,
    val listStory: List<MyStoryApp>
)

@Entity(tableName = "mystoryapp")
@Parcelize
data class MyStoryApp(
    val photoUrl: String,
    val name: String,
    val description: String,
    @PrimaryKey
    val id: String,
    val lat: Double,
    val lon: Double
) : Parcelable