package com.spacece.milestonetracker.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiModule {
     fun apiService(): ApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)   // server se connection time
            .readTimeout(120, TimeUnit.SECONDS)     // data read time
            .writeTimeout(120, TimeUnit.SECONDS)    // upload/write time
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)                         // attach client
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}