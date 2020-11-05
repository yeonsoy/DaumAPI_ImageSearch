package com.yeon.imagesearch

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class ImageApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}