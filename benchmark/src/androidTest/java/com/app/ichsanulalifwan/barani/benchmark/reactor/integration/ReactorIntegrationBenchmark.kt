package com.app.ichsanulalifwan.barani.benchmark.reactor.integration

import android.annotation.SuppressLint
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.ichsanulalifwan.barani.benchmark.api.IntegrationBenchmark
import com.app.ichsanulalifwan.barani.benchmark.reactor.ReactorBenchmark
import com.app.ichsanulalifwan.barani.core.utils.toNewsEntity
import org.junit.Test
import org.junit.runner.RunWith

@SuppressLint("CheckResult")
@RunWith(AndroidJUnit4::class)
class ReactorIntegrationBenchmark : ReactorBenchmark(), IntegrationBenchmark {

    @Test
    override fun integration1() = benchmarkRule.measureRepeated {
        runWithTimingDisabled { repository.deleteAllNews().block() }
        remoteDataSource.getTopHeadlines()
            .flatMap {
                repository.getEverythingNews(List(10) { "us" })
            }.flatMap { listResponse ->
                repository.insertNewsToDatabase(
                    listResponse.map { itemResponse ->
                        itemResponse.toNewsEntity()
                    }
                )
            }.block()
    }

    @Test
    override fun integration2() = benchmarkRule.measureRepeated {
        runWithTimingDisabled { repository.deleteAllNews().block() }
        remoteDataSource.getTopHeadlines()
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { listResponse ->
                repository.insertNewsToDatabase(
                    listResponse.map { itemResponse ->
                        itemResponse.toNewsEntity()
                    }
                ).then(
                    repository.insertNewsToDatabase(listResponse.map { itemResponse ->
                        itemResponse.toNewsEntity()
                    })
                )
            }.block()
    }

    @Test
    override fun integration3() = benchmarkRule.measureRepeated {
        runWithTimingDisabled { repository.deleteAllNews().block() }
        remoteDataSource.getTopHeadlines()
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { listResponse ->
                repository.insertNewsToDatabase(
                    listResponse.map { itemResponse ->
                        itemResponse.toNewsEntity()
                    }
                )
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
            }.block()
    }

    @Test
    override fun integration4() = benchmarkRule.measureRepeated {
        runWithTimingDisabled { repository.deleteAllNews().block() }
        remoteDataSource.getTopHeadlines()
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { remoteDataSource.getTopHeadlines() }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { repository.getEverythingNews(List(10) { "us" }) }
            .flatMap { listResponse ->
                repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() })
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
                    .then(repository.insertNewsToDatabase(listResponse.map { itemResponse -> itemResponse.toNewsEntity() }))
            }.block()
    }
}