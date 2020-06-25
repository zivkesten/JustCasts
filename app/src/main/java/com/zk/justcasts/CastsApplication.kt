package com.zk.justcasts

import android.app.Application
import com.zk.justcasts.repository.networking.networkModule
import com.zk.justcasts.repository.repositoryModule
import com.zk.justcasts.di.ViewModelsModule
import com.zk.justcasts.repository.apiModule
import com.zk.justcasts.repository.database.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CastsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CastsApplication)
            modules(listOf(
                ViewModelsModule.modules,
                repositoryModule,
                apiModule,
                networkModule,
                databaseModule
            ))
        }
    }
}