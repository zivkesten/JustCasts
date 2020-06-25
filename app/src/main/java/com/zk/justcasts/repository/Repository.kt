package com.zk.justcasts.repository

import android.util.Log
import com.google.gson.Gson
import com.zk.justcasts.models.BestPodcastsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.dsl.module
import java.io.IOException

val repositoryModule = module {

    single { Repository(get()) }
}

class Repository(private val networkApi: NetworkApi)  {

    private val searchError = "Error searching for movie"

    suspend fun getPodcastsASync(): BestPodcastsResponse {
        var podcastsResponse = BestPodcastsResponse(errorMessage = searchError)
        withContext(Dispatchers.IO) {
            try {
                val response = networkApi.getPodcastsASync()
                response.body()?.let { podcastsResponse = it }

                response.errorBody()?.let { body ->
                    podcastsResponse = Gson().fromJson(
                        body.string(),
                        BestPodcastsResponse::class.java
                    )
                }
            } catch (ex: IOException) {
                Log.w("Zivi", "search Movie fail", ex)
            }
        }
        return podcastsResponse
    }
}
