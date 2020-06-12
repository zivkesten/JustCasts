package com.zk.justcasts.repository

import com.zk.justcasts.models.BestPodcastsResponse
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.http.GET

val apiModule = module {
    single {
        val retrofit: Retrofit = get()
        retrofit.create(NetworkApi::class.java)
    }
}

interface NetworkApi {
    @GET("best_podcasts")
    suspend fun getPodcasts(): BestPodcastsResponse?
}
