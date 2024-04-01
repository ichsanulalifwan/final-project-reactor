package com.app.ichsanulalifwan.barani.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.ichsanulalifwan.barani.core.data.repository.location.AddressRepository
import com.app.ichsanulalifwan.barani.core.data.repository.news.rxjava.RxJavaNewsRepository
import com.app.ichsanulalifwan.barani.core.di.Injection
import com.app.ichsanulalifwan.barani.ui.beranda.BerandaViewModel
import com.app.ichsanulalifwan.barani.ui.list.ListViewModel
import com.app.ichsanulalifwan.barani.ui.profil.ProfilViewModel
import com.app.ichsanulalifwan.barani.ui.report.ReportViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val application: Application,
    private val newsRepository: RxJavaNewsRepository,
    private val addressRepository: AddressRepository,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(BerandaViewModel::class.java) -> {
                BerandaViewModel(application, newsRepository) as T
            }

            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(application, newsRepository) as T
            }

            modelClass.isAssignableFrom(ProfilViewModel::class.java) -> {
                ProfilViewModel() as T
            }

            modelClass.isAssignableFrom(ReportViewModel::class.java) -> {
                ReportViewModel() as T
            }

            else -> throw Throwable("Unknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application, isMock: Boolean): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    application,
                    Injection.provideRepository(application, isMock),
                    AddressRepository(application, isMock)
                ).apply {
                    instance = this
                }
            }
    }
}