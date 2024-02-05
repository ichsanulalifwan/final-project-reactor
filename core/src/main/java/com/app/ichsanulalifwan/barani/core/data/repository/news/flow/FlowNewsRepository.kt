package com.app.ichsanulalifwan.barani.core.data.repository.news.flow

import com.app.ichsanulalifwan.barani.core.data.source.local.entity.NewsEntity
import com.app.ichsanulalifwan.barani.core.data.source.local.entity.PublisherEntity
import com.app.ichsanulalifwan.barani.core.data.source.local.room.NewsDao
import com.app.ichsanulalifwan.barani.core.data.source.remote.network.flow.FlowNewsApiService
import com.app.ichsanulalifwan.barani.core.utils.toNewsEntity
import com.app.ichsanulalifwan.barani.core.utils.toPublisherEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FlowNewsRepository(
    private val remoteDataSource: FlowNewsApiService,
    private val localDataSource: NewsDao,
) {

    val news: Flow<List<NewsEntity>> = localDataSource.allNewsByFlow()

    val publishers: Flow<List<PublisherEntity>> = localDataSource.allPublisherByFlow()

    fun getTopHeadlineNews(countryCode: String, category: String) = flow<Unit> {
        val newsResponse = remoteDataSource.getTopHeadlines(
            country = countryCode,
            category = category,
        )
        val articles = newsResponse.articles
        insertNewsToDatabase(newsEntities = articles.map {
            it.toNewsEntity()
        })
    }.flowOn(Dispatchers.IO)

    fun getEverythingNews(countryCode: String) = flow<Unit> {
        val response = remoteDataSource.getEverything(country = countryCode)
        val articles = response.articles
        insertNewsToDatabase(newsEntities = articles.map {
            it.toNewsEntity()
        })
    }.flowOn(Dispatchers.IO)

    fun getNewsPublisher() = flow<Unit> {
        val response = remoteDataSource.getNewsPublishers()
        val sources = response.sources
        insertPublisherToDatabase(publisherEntities = sources.map {
            it.toPublisherEntity()
        })
    }.flowOn(Dispatchers.IO)

    suspend fun insertNewsToDatabase(newsEntities: List<NewsEntity>) {
        localDataSource.apply {
            deleteAllNews()
            insertNews(newsEntities = newsEntities)
        }
    }

    suspend fun insertPublisherToDatabase(publisherEntities: List<PublisherEntity>) {
        localDataSource.apply {
            deleteAllPublishers()
            insertPublisher(publisherEntities = publisherEntities)
        }
    }

    companion object {
        @Volatile
        private var instance: FlowNewsRepository? = null

        fun getInstance(
            remoteDataSource: FlowNewsApiService,
            localDataSource: NewsDao,
        ): FlowNewsRepository =
            instance ?: synchronized(this) {
                instance ?: FlowNewsRepository(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource,
                ).apply { instance = this }
            }
    }
}