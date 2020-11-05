package com.yeon.imagesearch.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.yeon.imagesearch.api.ImageRepository
import com.yeon.imagesearch.model.ImageDataSource
import com.yeon.imagesearch.model.ImageDataSourceFactory
import com.yeon.imagesearch.model.ImageModel
import com.yeon.imagesearch.api.NetworkState
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ImageViewModel(application: Application, viewModelInterface: ImageViewModelInterface) : BaseViewModel<ImageModel, ImageViewModel.ImageViewModelInterface, String>(application, viewModelInterface) {
    private val executor: Executor
    var netWorkState: LiveData<NetworkState>? = null
    var refreshState: LiveData<NetworkState>? = null
    var dataState: LiveData<Boolean>? = null
    var dataLayoutSubject: PublishSubject<Boolean> // 구독 이후 갱신된 값에 대해서만 전달 받는 객체
    lateinit var documentsList: LiveData<PagedList<ImageModel.Documents>>
    lateinit var imageListDataSource: ImageDataSourceFactory

    init {
        executor = Executors.newFixedThreadPool(10)
        dataLayoutSubject = PublishSubject.create()
    }

    override fun getSingle(): Single<ImageModel> = null!!

    override fun getSingle(args: String): Single<ImageModel> {
        return ImageRepository.instance.getResponse(args)
    }

    fun getImages(paging: String, sort: String) {
        imageListDataSource = ImageDataSourceFactory(compositeDisposable, paging, sort, viewModelInterface)
        netWorkState = Transformations.switchMap<ImageDataSource, NetworkState>(imageListDataSource.sourceFactoryLiveData) { it.networkStateLiveData }
        refreshState = Transformations.switchMap<ImageDataSource, NetworkState>(imageListDataSource.sourceFactoryLiveData) { it.initialLoad }
        dataState = Transformations.switchMap<ImageDataSource, Boolean>(imageListDataSource.sourceFactoryLiveData) { it.isData }
        documentsList = LivePagedListBuilder(imageListDataSource, 20)
            .setFetchExecutor(executor)
            .build()

        viewModelInterface.getImages(documentsList)
    }

    fun retry() {
        imageListDataSource.sourceFactoryLiveData.value!!.retry()
    }

    class ImageViewModelFactory(private val mApplication: Application, private val viewModelInterface: ImageViewModelInterface) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
                ImageViewModel(mApplication, viewModelInterface) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

    interface ImageViewModelInterface : BaseVewModelInterface {
        fun getImages(items: LiveData<PagedList<ImageModel.Documents>>)
    }

    override fun onCleared() {
        super.onCleared()
        dataLayoutSubject.onComplete()
    }
}