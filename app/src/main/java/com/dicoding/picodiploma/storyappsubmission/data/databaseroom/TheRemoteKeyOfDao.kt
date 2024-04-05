package com.dicoding.picodiploma.storyappsubmission.data.databaseroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TheRemoteKeyOfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOfTheStories(remoteKey: List<TheRemoteKey>)

    @Query("SELECT * FROM key_remote WHERE id = :id")
    suspend fun getTheRemoteIdKey(id: String): TheRemoteKey?

    @Query("DELETE FROM key_remote")
    suspend fun deleteTheRemoteKey()
}