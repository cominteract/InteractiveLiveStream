package com.ainsigne.interactivelivestreaming.api;


import com.ainsigne.interactivelivestreaming.di.RxSingleSchedulers
import com.ainsigne.interactivelivestreaming.utils.BASE_API
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * [StreamsAPI] declaration of its retrofit service builder for making http calls
 */
class StreamsAPI {



    val webservice: StreamsService by lazy {


        val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build()


        Retrofit.Builder()
            .baseUrl(BASE_API).client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(StreamsService::class.java)
    }




}

