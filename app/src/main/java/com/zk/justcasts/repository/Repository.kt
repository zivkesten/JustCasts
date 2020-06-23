package com.zk.justcasts.repository

import com.google.gson.Gson
import com.zk.justcasts.models.BestPodcastsResponse
import io.reactivex.Observable
import org.koin.dsl.module

val repositoryModule = module {

    single { Repository(get()) }
}

class Repository(private val networkApi: NetworkApi)  {
   fun getPodcasts(): Observable<BestPodcastsResponse?> {
       return networkApi.getPodcasts()
           .map { response ->
               response.body()?.let { return@map it }
               response.errorBody()?.let { body ->
                   return@map Gson().fromJson(
                       body.string(),
                       BestPodcastsResponse::class.java
                   )
               }
           }
   }
}
