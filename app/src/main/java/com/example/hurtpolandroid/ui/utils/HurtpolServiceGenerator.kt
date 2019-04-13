package com.example.hurtpolandroid.ui.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class HurtpolServiceGenerator {
    private val BASE_URL = "http://192.168.43.245:8080/user-portal/auth/"

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        )
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())

    private var retrofit = builder.build()

    private val httpClient = OkHttpClient.Builder()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    fun <S> createService(serviceClass: Class<S>, token: String?): S {
        if (token != null) {
            httpClient.interceptors().clear()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val builder1 = original.newBuilder()
                    .header("Authorization", token)
                val request = builder1.build()
                chain.proceed(request)
            }
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }
}