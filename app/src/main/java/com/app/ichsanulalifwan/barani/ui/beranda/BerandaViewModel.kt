package com.app.ichsanulalifwan.barani.ui.beranda

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.app.ichsanulalifwan.barani.core.R
import com.app.ichsanulalifwan.barani.core.data.repository.location.AddressRepository
import com.app.ichsanulalifwan.barani.core.data.repository.news.reactor.ReactorNewsRepository
import com.app.ichsanulalifwan.barani.core.model.News
import com.app.ichsanulalifwan.barani.core.model.Publisher
import com.app.ichsanulalifwan.barani.core.utils.DataMapper
import com.app.ichsanulalifwan.barani.core.viewmodel.BaseViewModel
import com.app.ichsanulalifwan.barani.utils.Constant.HEALTH_CATEGORY
import com.app.ichsanulalifwan.barani.utils.Constant.US_COUNTRY_CODE
import reactor.core.Disposable
import reactor.core.Disposables
import reactor.core.scheduler.Schedulers

class BerandaViewModel(
    application: Application,
    private val newsRepository: ReactorNewsRepository,
    private val addressRepository: AddressRepository,
) : BaseViewModel(application) {

    private var disposable = Disposables.composite()
    private var locationDisposable: Disposable? = null

    init {
        getTopHeadlineNews()
    }

    override fun getNews(): LiveData<List<News>> = newsRepository.news
        .map { entityList ->
            DataMapper.mapNewsEntityToModel(entityList)
        }
        .subscribeOn(Schedulers.boundedElastic())
        .toLiveData()

    override fun getPublishers(): LiveData<List<Publisher>> = newsRepository.publishers
        .map { entityList ->
            DataMapper.mapPublisherListToModel(entityList)
        }
        .toLiveData()

    override fun getTopHeadlineNews() {
        startTopHeadlinesNewsTimer()
        newsRepository.getTopHeadlineNews(US_COUNTRY_CODE, HEALTH_CATEGORY)
            .doOnSubscribe { isLoading.value = true }
            .doFinally {
                stopTopHeadlinesNewsTimer()
                isLoading.value = false
            }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(
                { isLocalNews.value = false },
                { throwable ->
                    message.value = context.getString(R.string.news_error)
                    Log.e(LOG_TAG, "Could not fetch news", throwable)
                }
            ).also { disposable.add(it) }
    }

    override fun getNewsPublisher() {
        startSourcesNewsTimer()

        newsRepository.getNewsPublisher()
            .subscribeOn(Schedulers.boundedElastic())
            .publishOn(Schedulers.single())
            .doOnSubscribe { isLoading.value = true }
            .doFinally {
                stopSourcesNewsTimer()
                isLoading.value = false
            }
            .subscribe(
                { isLocalNews.value = false },
                { throwable ->
                    message.value = context.getString(R.string.news_error)
                    Log.e(LOG_TAG, "Could not fetch publisher", throwable)
                }
            )
            .also {
                disposable.add(it)
            }
    }

    override fun startUpdatesForTopHeadlineNewsLocal() {
        TODO("Not yet implemented")
    }

    override fun cancelUpdatesForTopHeadlineNewsLocal() {
        locationDisposable?.dispose()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}