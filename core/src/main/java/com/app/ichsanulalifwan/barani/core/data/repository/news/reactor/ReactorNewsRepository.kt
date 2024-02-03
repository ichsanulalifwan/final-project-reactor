package com.app.ichsanulalifwan.barani.core.data.repository.news.reactor

import com.app.ichsanulalifwan.barani.core.data.source.local.entity.NewsEntity
import com.app.ichsanulalifwan.barani.core.data.source.local.entity.PublisherEntity
import com.app.ichsanulalifwan.barani.core.data.source.local.room.NewsDao
import com.app.ichsanulalifwan.barani.core.data.source.remote.network.reactor.ReactorNewsApiService
import com.app.ichsanulalifwan.barani.core.utils.toNewsEntity
import com.app.ichsanulalifwan.barani.core.utils.toPublisherEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ReactorNewsRepository(
    private val remoteDataSource: ReactorNewsApiService,
    private val localDataSource: NewsDao,
) {

    val news: Flux<List<NewsEntity>>
        get() = localDataSource.allNewsByFlux()

    val publishers: Flux<List<PublisherEntity>>
        get() = localDataSource.allPublisherByFlux()

    fun getTopHeadlineNews(countryCode: String, category: String): Mono<Void> =
        remoteDataSource.getTopHeadlines(country = countryCode, category = category)
            .flatMap { newsResponse ->
                insertNewsToDatabase(newsResponse.articles.map {
                    it.toNewsEntity()
                })
            }

    fun getEverythingNews(countryCode: String): Mono<Void> =
        remoteDataSource.getEverything(country = countryCode)
            .flatMap { response ->
                insertNewsToDatabase(response.articles.map {
                    it.toNewsEntity()
                })
            }

    fun getNewsPublisher(): Mono<Void> =
        remoteDataSource.getNewsPublishers()
            .flatMap { response ->
                insertPublisherToDatabase(response.sources.map {
                    it.toPublisherEntity()
                })
            }

    fun insertNewsToDatabase(newsEntities: List<NewsEntity>): Mono<Void> =
        localDataSource.deleteAllNewsAsMono()
            .then(
                localDataSource.insertNewsAsMono(newsEntities)
            )

    fun insertPublisherToDatabase(publisherEntities: List<PublisherEntity>): Mono<Void> =
        localDataSource.deleteAllPublishersAsMono()
            .then(
                localDataSource.insertPublisherAsMono(publisherEntities)
            )

    companion object {
        @Volatile
        private var instance: ReactorNewsRepository? = null

        fun getInstance(
            remoteDataSource: ReactorNewsApiService,
            localDataSource: NewsDao,
        ): ReactorNewsRepository =
            instance ?: synchronized(this) {
                instance ?: ReactorNewsRepository(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource,
                ).apply { instance = this }
            }
    }
}