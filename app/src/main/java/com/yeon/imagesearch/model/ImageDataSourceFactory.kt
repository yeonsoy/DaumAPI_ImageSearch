package com.yeon.imagesearch.model

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.yeon.imagesearch.viewmodel.ImageViewModel
import io.reactivex.disposables.CompositeDisposable


class ImageDataSourceFactory(private val compositeDisposable: CompositeDisposable, private val query: String, private val sort: String, private val viewModelInterface: ImageViewModel.ImageViewModelInterface)
    : DataSource.Factory<Int, ImageModel.Documents>() {

    val sourceFactoryLiveData = MutableLiveData<ImageDataSource>()
    override fun create(): DataSource<Int, ImageModel.Documents> {
        val dataSource = ImageDataSource(compositeDisposable, query, sort, viewModelInterface)
        sourceFactoryLiveData.postValue(dataSource)
        return dataSource
    }

}