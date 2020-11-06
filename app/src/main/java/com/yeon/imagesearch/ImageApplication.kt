package com.yeon.imagesearch

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig

class ImageApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        var config = ImagePipelineConfig.newBuilder(this)
                .build()

        Fresco.initialize(this, config)
    }
}