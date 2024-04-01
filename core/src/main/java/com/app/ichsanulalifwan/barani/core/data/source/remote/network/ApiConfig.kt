package com.app.ichsanulalifwan.barani.core.data.source.remote.network

import android.content.Context
import com.app.ichsanulalifwan.barani.core.data.source.remote.network.reactor.ReactorNewsApiService
import com.app.ichsanulalifwan.barani.core.data.source.remote.network.rxjava.RxJavaNewsApiService
import com.app.ichsanulalifwan.barani.core.mock.MockInterceptor
import com.app.ichsanulalifwan.barani.core.utils.Constant.BASE_URL
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private fun createHttpClient(interceptor: Interceptor? = null): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .apply { interceptor?.let { addInterceptor(it) } }
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ReactorCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    fun getApiService(): RxJavaNewsApiService {
        val client = createHttpClient()
        val retrofit = createRetrofit(client)
        return retrofit.create(RxJavaNewsApiService::class.java)
    }

    fun getMockApiService(context: Context): RxJavaNewsApiService {
        val mockInterceptor = MockInterceptor(context = context)
        val client = createHttpClient(mockInterceptor)
        val retrofit = createRetrofit(client)
        return retrofit.create(RxJavaNewsApiService::class.java)
    }

    fun getMockBenchmarkApiService(context: Context): ReactorNewsApiService {
        val mockInterceptor = MockInterceptor(context = context)
        val client = createHttpClient(mockInterceptor)
        val retrofit = createRetrofit(client)
        return retrofit.create(ReactorNewsApiService::class.java)
    }

}