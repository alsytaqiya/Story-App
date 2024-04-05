package com.dicoding.picodiploma.storyappsubmission.data.databaseroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp

@Database(
    entities = [MyStoryApp::class, TheRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseOfStory : RoomDatabase() {
    abstract fun dataAccessObjectStory(): DaoOfMyStory
    abstract fun dataAccessObjectKeyRemote(): TheRemoteKeyOfDao

    companion object {
        @Volatile
        private var INSTANCESTORY: DatabaseOfStory? = null

        @JvmStatic
        fun getInstance(context: Context): DatabaseOfStory {
            return INSTANCESTORY ?: synchronized(this) {
                INSTANCESTORY ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseOfStory::class.java, "db_story.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCESTORY = it }
            }
        }

    }
}