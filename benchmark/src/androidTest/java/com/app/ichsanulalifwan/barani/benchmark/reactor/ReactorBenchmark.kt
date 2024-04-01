package com.app.ichsanulalifwan.barani.benchmark.reactor

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.test.platform.app.InstrumentationRegistry
import com.app.ichsanulalifwan.barani.core.data.repository.news.reactor.ReactorNewsRepository
import com.app.ichsanulalifwan.barani.core.data.source.local.room.AppDatabase
import com.app.ichsanulalifwan.barani.core.data.source.local.room.NewsDao
import com.app.ichsanulalifwan.barani.core.data.source.remote.network.ApiConfig
import com.app.ichsanulalifwan.barani.core.data.source.remote.network.reactor.ReactorNewsApiService
import org.junit.Rule

open class ReactorBenchmark {

    protected val remoteDataSource: ReactorNewsApiService by lazy {
        ApiConfig.getMockBenchmarkApiService(context = context)
    }

    protected val localDataSource: NewsDao by lazy {
        AppDatabase.getInstance(context = context).newsDao()
    }

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val repository = ReactorNewsRepository(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource,
    )
}