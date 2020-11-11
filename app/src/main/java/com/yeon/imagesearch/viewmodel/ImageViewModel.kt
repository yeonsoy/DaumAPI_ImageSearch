package com.yeon.imagesearch.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.yeon.imagesearch.model.ImageDataSource
import com.yeon.imagesearch.model.ImageDataSourceFactory
import com.yeon.imagesearch.model.ImageModel
import com.yeon.imagesearch.api.ImageRepository
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
    lateinit var userList: LiveData<PagedList<ImageModel.Documents>>
    lateinit var imageListDataSource: ImageDataSourceFactory
    var dataLayoutSubject: PublishSubject<Boolean>

    init {
        executor = Executors.newFixedThreadPool(5)
        dataLayoutSubject = PublishSubject.create()
    }


    override fun getSingle(): Single<ImageModel> {
        return null!!
    }

    override fun getSingle(args: String): Single<ImageModel> {
        return ImageRepository.instance.getResponse(args)
    }

    fun getImagesPaging(paging: String, sort: String) {
        imageListDataSource = ImageDataSourceFactory(compositeDisposable, paging, sort, viewModelInterface)
        netWorkState = Transformations.switchMap<ImageDataSource, NetworkState>(imageListDataSource.sourceFactoryLiveData) { it.networkStateLiveData }
        refreshState = Transformations.switchMap<ImageDataSource, NetworkState>(imageListDataSource.sourceFactoryLiveData) { it.initialLoad }
        dataState = Transformations.switchMap<ImageDataSource, Boolean>(imageListDataSource.sourceFactoryLiveData) { it.isData }
        userList = LivePagedListBuilder(imageListDataSource, 30)
                .setFetchExecutor(executor)
                .build()

        viewModelInterface.getImages(userList)
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

    interface ImageViewModelInterface : BaseViewModelInterface {
        fun getImages(items: LiveData<PagedList<ImageModel.Documents>>)
    }

    override fun onCleared() {
        super.onCleared()
        dataLayoutSubject.onComplete()
    }
}