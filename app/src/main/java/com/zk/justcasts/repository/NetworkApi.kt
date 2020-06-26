package com.zk.justcasts.repository

import com.zk.justcasts.models.BestPodcastsResponse
import com.zk.justcasts.models.PodcastResponse
import org.koin.dsl.module
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("podcasts/{id}")
    suspend fun getEpisodes(
        @Path("id") podcastId: String
    ): Response<PodcastResponse?>

}
