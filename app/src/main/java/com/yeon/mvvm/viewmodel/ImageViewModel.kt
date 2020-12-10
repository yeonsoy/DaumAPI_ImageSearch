package com.yeon.mvvm.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeon.mvvm.model.Documents
import com.yeon.mvvm.model.Result
import com.yeon.mvvm.util.DaumAPI
import com.yeon.mvvm.view.epoxy.ImageController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageViewModel() : ViewModel(), KoinComponent {

    private val _inputImage = MutableLiveData<ArrayList<Documents>>()
    val inputImage: LiveData<ArrayList<Documents>>
        get() = _inputImage

    val api: DaumAPI by inject()
    var query: String? = null
    var sort: String? = "accuracy"
    var page: Int? = 1
    var size: Int? = 80
    var is_end = false

    fun getImageList(query: String, sort: String? = "accuracy", page: Int? = 1, size: Int? = 1) {

        api.getImageList(query, sort, page, size).enqueue(object : Callback<Result> {
            override fun onResponse(
                    call: Call<Result>,
                    response: Response<Result>
            ) {
                if (response.code() != 200) {
                    Log.d(TAG, "onResponse: 오류")
                } else {
                    val imageResponse = response.body()
                    imageResponse?.let { _inputImage.value = getImageDocuments(it) }
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                // 실패
                Log.d(TAG, "onFailure: 실패")
            }
        })
    }

    fun fetchData(
        query: String,
        sort: String? = "accuracy",
        page: Int? = 1,
        size: Int? = 80,
        viewController: ImageController?,
    ) {
        api.getImageList(query, sort, page, size).enqueue(object : Callback<Result> {
            override fun onResponse(
                call: Call<Result>,
                response: Response<Result>
            ) {
                if (response.code() != 200) {
                    Log.d(TAG, "onResponse: 오류")
                } else {
                    response.body()?.let {
                        if (_inputImage.value == null)
                            _inputImage.value = getImageDocuments(it)
                        else
                            _inputImage.value?.addAll(getImageDocuments(it))

                        is_end = isLastPage(it)
                        if (is_end)
                            viewController?.setLoadingMore(false)
                    }

                    viewController?.requestModelBuild()
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                // 실패
                Log.d(TAG, "onFailure: 실패")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
    }

    private fun getImageDocuments(result: Result): ArrayList<Documents> = result.documents
    private fun isLastPage(result: Result): Boolean = result.meta.is_end
}