package com.yeon.imagesearch.api

import com.yeon.imagesearch.model.ImageModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestAPI {
    @GET("/v2/search/image")
    fun getSearchImage(@QueryMap value : Map<String, String>) : Single<ImageModel>
}