package com.yeon.mvvm.di

import com.yeon.mvvm.viewmodel.ImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ImageViewModel()
    }
}
