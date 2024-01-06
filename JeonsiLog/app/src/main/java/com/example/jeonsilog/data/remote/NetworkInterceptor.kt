package com.example.jeonsilog.data.remote

import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.networkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class NetworkInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            CoroutineScope(Dispatchers.Main).launch{
                networkState.value = true
            }

            chain.proceed(request)
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch{
                networkState.value = false
            }
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(599)
                .message(e.message ?: "NetworkDialog")
                .body((e.message ?: "NetworkDialog").toResponseBody(null))
                .build()
        }
    }
}