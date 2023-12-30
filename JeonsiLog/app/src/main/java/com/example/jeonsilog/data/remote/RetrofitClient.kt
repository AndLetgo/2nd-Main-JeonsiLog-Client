package com.example.jeonsilog.data.remote

import com.example.jeonsilog.BuildConfig
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    private fun loggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

    private fun tokenRefreshInterceptor(): Authenticator {
        return TokenRefreshInterceptor()
    }

    private val interceptor = OkHttpClient().newBuilder()
        .addNetworkInterceptor(loggingInterceptor())
        .authenticator(tokenRefreshInterceptor())
        .build()


    fun getRetrofit(): Retrofit? {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(interceptor)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
