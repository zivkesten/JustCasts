package com.zk.justcasts.repository

import com.zk.justcasts.models.BestPodcastsResponse
import org.koin.dsl.module
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

val apiModule = module {
    single {
        val retrofit: Retrofit = get()
        retrofit.create(NetworkApi::class.java)
    }
}

interface NetworkApi {

    @GET("best_podcasts")
    suspend fun getPodcastsASync(): Response<BestPodcastsResponse?>

    @GET("podcasts")
    suspend fun getPodcast(
        @Query("podcasts") podcastId: String
    ): Response<BestPodcastsResponse?>

}
