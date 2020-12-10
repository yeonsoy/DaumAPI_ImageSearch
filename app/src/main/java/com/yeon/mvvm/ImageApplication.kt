package com.yeon.mvvm

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.yeon.mvvm.di.appModule
import com.yeon.mvvm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ImageApplication : Application() {
    override fun onCreate(){
        super.onCreate()
       startKoin {
            androidContext(this@ImageApplication)
            modules(listOf(appModule, viewModelModule))
        }
        Fresco.initialize(this);
    }
}