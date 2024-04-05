package com.dicoding.picodiploma.storyappsubmission.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.picodiploma.storyappsubmission.api.AppProgrammingInterface
import com.dicoding.picodiploma.storyappsubmission.data.databaseroom.DatabaseOfStory
import com.dicoding.picodiploma.storyappsubmission.data.databaseroom.TheRemoteKey
import com.dicoding.picodiploma.storyappsubmission.data.responsedata.MyStoryApp

@OptIn(ExperimentalPagingApi::class)
class RemoteStoryOfModerator(
    private val databaseOfStory: DatabaseOfStory,
    private val appProgrammingInterface: AppProgrammingInterface,
    private val token: String
) : RemoteMediator<Int, MyStoryApp>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MyStoryApp>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getKeyInTheClosePosition(state)
                remoteKey?.nextKey?.minus(1) ?: INITIAL_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKey = getKeyInTheFirstItem(state)
                val keyPrev = remoteKey?.previousKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                keyPrev
            }
            LoadType.APPEND -> {
                val remoteKey = getKeyInTheLastItem(state)
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }
        return try {
            val responseOfData =
                appProgrammingInterface.getMyStoryApp(
                    "Bearer $token",
                    page,
                    state.config.pageSize
                ).listStory
            val endOfPaginationReached = responseOfData.isEmpty()

            databaseOfStory.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    databaseOfStory.dataAccessObjectKeyRemote().deleteTheRemoteKey()
                    databaseOfStory.dataAccessObjectStory().deleteAll()
                }

                val previousKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = responseOfData.map {
                    TheRemoteKey(id = it.id, previousKey = previousKey, nextKey = nextKey)
                }

                databaseOfStory.dataAccessObjectKeyRemote().insertAllOfTheStories(keys)
                databaseOfStory.dataAccessObjectStory().createMyStory(responseOfData)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }


    private suspend fun getKeyInTheLastItem(stateRemote: PagingState<Int, MyStoryApp>): TheRemoteKey? {
        return stateRemote.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            databaseOfStory.dataAccessObjectKeyRemote().getTheRemoteIdKey(it.id)
        }
    }

    private suspend fun getKeyInTheFirstItem(stateRemote: PagingState<Int, MyStoryApp>): TheRemoteKey? {
        return stateRemote.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            databaseOfStory.dataAccessObjectKeyRemote().getTheRemoteIdKey(it.id)
        }
    }

    private suspend fun getKeyInTheClosePosition(stateRemote: PagingState<Int, MyStoryApp>): TheRemoteKey? {
        return stateRemote.anchorPosition?.let { position ->
            stateRemote.closestItemToPosition(position)?.id?.let {
                databaseOfStory.dataAccessObjectKeyRemote().getTheRemoteIdKey(it)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}