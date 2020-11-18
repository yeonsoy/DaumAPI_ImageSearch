package com.yeon.mvvm.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeon.mvvm.model.Documents
import com.yeon.mvvm.model.ImageModel
import com.yeon.mvvm.util.DaumAPI
import com.yeon.mvvm.util.DaumAPI.Companion.getImageDocuments
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageViewModel() : ViewModel() {

    private val _inputImage = MutableLiveData<ArrayList<Documents>>()
    val inputImage: LiveData<ArrayList<Documents>>
        get() = _inputImage

    val api: DaumAPI by inject()

    fun getImageList(query: String, sort: String? = "accuracy", page: Int? = 1, size: Int? = 1) {
        // retrofit2 만을 사용한 예외처리
        api.getImageList(query, sort, page, size).enqueue(object : Callback<ImageModel.Result> {
            override fun onResponse(
                    call: Call<ImageModel.Result>,
                    response: Response<ImageModel.Result>
            ) {
                if (response.message() != "OK") {
                    // 오류
                } else {
                    val imageResponse = response.body()
                    imageResponse?.let { _inputImage.value = getImageDocuments(it) }
                }
            }

            override fun onFailure(call: Call<ImageModel.Result>, t: Throwable) {
                // 실패
                Log.d(TAG, "onFailure: 실패")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
    }
}