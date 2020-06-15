package com.zk.justcasts.repository

import com.zk.justcasts.models.BestPodcastsResponse
import io.reactivex.Observable
import org.koin.dsl.module
import retrofit2.Response
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
    fun getPodcasts(): Observable<Response<BestPodcastsResponse?>>
}
