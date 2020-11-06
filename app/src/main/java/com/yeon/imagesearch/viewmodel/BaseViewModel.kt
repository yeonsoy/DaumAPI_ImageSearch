package com.yeon.imagesearch.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.yeon.imagesearch.model.BaseModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<T : BaseModel, I : BaseViewModelInterface, A> (application : Application, var viewModelInterface: I):AndroidViewModel(application) {
    protected abstract fun getSingle(): Single<T>
    protected abstract fun getSingle(args: A): Single<T>
    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}