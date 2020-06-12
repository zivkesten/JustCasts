package com.zk.justcasts.repository

import com.zk.justcasts.models.BestPodcastsResponse
import org.koin.dsl.module

val repositoryModule = module {

    single { Repository(get()) }
}

class Repository(private val networkApi: NetworkApi)  {

   suspend fun getPodcasts(): BestPodcastsResponse? {
       return networkApi.getPodcasts()
   }
}
