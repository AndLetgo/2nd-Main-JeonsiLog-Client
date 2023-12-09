package com.example.jeonsilog.data.remote

import com.example.jeonsilog.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    private fun loggingInterceptor(): Interceptor{
        val interceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG){
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

    private val loggingInterceptor = OkHttpClient().newBuilder()
        .addNetworkInterceptor(loggingInterceptor())
        .build()

    fun getRetrofit(): Retrofit? {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(loggingInterceptor)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

//class ResponseInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//
//        // 통신 전 로깅
//        Log.d("Retrofit", "Request: ${request.url}")
//
//        val response = try {
//            chain.proceed(request)
//        } catch (e: Exception) {
//            Log.e("Retrofit", "Error: ${e.message}")
//            throw e
//        }
//
//        Log.d("Retrofit", "Response: ${response.code}")
//        return response
//    }
//}
