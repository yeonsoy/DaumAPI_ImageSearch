package com.yeon.imagesearch.viewmodel

import io.reactivex.disposables.Disposable

interface BaseViewModelInterface {
    fun putDisposableMap(tag: String, disposable: Disposable)
    fun removeDisposable(tag: String)
}