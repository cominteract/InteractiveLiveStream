package com.ainsigne.interactivelivestreaming.di

import com.ainsigne.interactivelivestreaming.api.StreamsService
import com.ainsigne.interactivelivestreaming.utils.BASE_API
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { createWebService() }
}

fun createWebService() : StreamsService {
    val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build()


    return Retrofit.Builder()
        .baseUrl(BASE_API).client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(StreamsService::class.java)
}
