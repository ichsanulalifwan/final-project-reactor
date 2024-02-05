package com.app.ichsanulalifwan.barani.core.data.source.remote.network

import android.content.Context
import com.app.ichsanulalifwan.barani.core.data.source.remote.network.rxjava.RxJavaNewsApiService
import com.app.ichsanulalifwan.barani.core.mock.MockInterceptor
import com.app.ichsanulalifwan.barani.core.utils.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    fun getApiService(): RxJavaNewsApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(client)
            .build()

        return retrofit.create(RxJavaNewsApiService::class.java)
    }

    fun getMockApiService(context: Context): RxJavaNewsApiService {

        val mockInterceptor = MockInterceptor(context = context)

        val mockClient = OkHttpClient.Builder()
            .addInterceptor(mockInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(mockClient)
            .build()

        return retrofit.create(RxJavaNewsApiService::class.java)
    }

}