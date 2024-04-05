package com.dicoding.picodiploma.storyappsubmission.data.databaseroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_remote")
data class TheRemoteKey(
    @PrimaryKey val id: String,
    val previousKey: Int?,
    val nextKey: Int?
)

