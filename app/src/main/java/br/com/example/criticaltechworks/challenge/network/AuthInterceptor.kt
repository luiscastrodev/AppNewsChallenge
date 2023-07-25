package br.com.example.criticaltechworks.challenge.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("x-api-key", apiKey)
            .build()
        return chain.proceed(modifiedRequest)
    }
}