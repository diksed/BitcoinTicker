package com.sedatkavak.bitcointicker.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(request().newBuilder().addHeader("X-CMC_PRO_API_KEY", "784cb7ca-4485-4e85-9665-555f0f857873").build())
    }
}