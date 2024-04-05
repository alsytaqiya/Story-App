package com.dicoding.picodiploma.storyappsubmission.data.databaseroom

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp

@Dao
interface DaoOfMyStory {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createMyStory(storyEntity: List<MyStoryApp>)

    @Query("SELECT * FROM mystoryapp")
    fun getMyStoryApp(): PagingSource<Int, MyStoryApp>

    @Query("DELETE FROM mystoryapp")
    suspend fun deleteAll()
}