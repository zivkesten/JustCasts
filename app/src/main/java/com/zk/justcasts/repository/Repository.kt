package com.zk.justcasts.repository

import android.util.Log
import com.google.gson.Gson
import com.zk.justcasts.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.dsl.module
import java.io.IOException
import java.lang.NullPointerException

val repositoryModule = module {

    single { Repository(get()) }
}

class Repository(private val networkApi: NetworkApi)  {

    private val searchError = "Error searching"

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
                Log.w("Zivi", "get best podcasts fail", ex)
            }
        }
        return podcastsResponse
    }

    suspend fun getEpisodesASync(podcastsId: String): List<EpisodeDTO>? {
        var podcast = PodcastResponse(errorMessage = searchError)
        withContext(Dispatchers.IO) {
            try {
                val response = networkApi.getEpisodes(podcastsId)
                response.body()?.let {
                    Log.w("Zivi", "episodesd response $it")
                    podcast = it
                }

                response.errorBody()?.let { body ->
                    podcast = Gson().fromJson(body.string(), PodcastResponse::class.java)
                }
            } catch (ex: IOException) {
                Log.w("Zivi", "get episodes fail", ex)
            } catch (ex: NullPointerException) {
                Log.w("Zivi", "get episodes fail", ex)
            }
        }
        return podcast.episodes
    }
}
