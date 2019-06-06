package com.example.hurtpolandroid.ui.utils

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class HurtpolServiceGenerator {
    private val BASE_URL = "https://userportal.radekg96.com/"
    //    private val BASE_URL = "http://192.168.0.102:8080/user-portal/"
    private var logging = HttpLoggingInterceptor()

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

    fun <S> createServiceWithToken(serviceClass: Class<S>, context: Context): S {
        val token = getToken(context)
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        httpClient.addInterceptor(logging)
        if (token != "") {
            httpClient.interceptors().clear()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val builder1 = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                val request = builder1.build()
                chain.proceed(request)
            }
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }

    private fun getToken(context: Context): String {
        val preferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return preferences.getString("token", "")
    }


}