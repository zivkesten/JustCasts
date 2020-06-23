package com.zk.justcasts.networking

import com.zk.justcasts.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { provideOkHttpClient() }
    single { provideRetrofit(get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

private fun provideOkHttpClient(): OkHttpClient {
    val connectTimeout = 15 // 15s
    val readTimeout = 15 // 15s

    val builder = OkHttpClient().newBuilder()
        .connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS)
        .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
		builder.addInterceptor(httpLoggingInterceptor)
        builder.addInterceptor(AuthInterceptor())
    }
    return builder.build()
}
