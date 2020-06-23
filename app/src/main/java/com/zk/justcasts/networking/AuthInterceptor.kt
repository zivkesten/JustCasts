package com.zk.justcasts.networking

import com.zk.justcasts.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

private const val KEY = BuildConfig.API_KEY

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("X-ListenAPI-Key", KEY)
                .build()
        )
}