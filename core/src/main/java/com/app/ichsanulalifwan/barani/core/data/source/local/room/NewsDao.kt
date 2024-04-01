package com.app.ichsanulalifwan.barani.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.ichsanulalifwan.barani.core.data.source.local.entity.NewsEntity
import com.app.ichsanulalifwan.barani.core.data.source.local.entity.PublisherEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface NewsDao {

    /* RxJava */

    /**
     * Load data.
     */
    @Query("SELECT * FROM news_entities")
    fun allNewsByFlowable(): Flowable<List<NewsEntity>>

    @Query("SELECT * FROM publisher_entities")
    fun allPublisherByFlowable(): Flowable<List<PublisherEntity>>

    /**
     * Insert data.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsAsCompletable(newsEntities: List<NewsEntity>): Completable

    @Query("DELETE FROM news_entities")
    fun deleteNewsAsCompletable(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPublisherAsCompletable(publisherEntities: List<PublisherEntity>): Completable

    @Query("DELETE FROM publisher_entities")
    fun deletePublisherAsCompletable(): Completable

    /* Reactor */

    /**
     * Load data.
     */
    @Query("SELECT * FROM news_entities")
    fun allNewsByFlux(): Flowable<List<NewsEntity>>

    @Query("SELECT * FROM publisher_entities")
    fun allPublisherByFlux(): Flowable<List<PublisherEntity>>

    /**
     * Insert data.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(newsEntities: List<NewsEntity>): Completable

    @Query("DELETE FROM news_entities")
    fun deleteAllNews(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPublisher(publisherEntities: List<PublisherEntity>): Completable

    @Query("DELETE FROM publisher_entities")
    fun deleteAllPublishers(): Completable
}