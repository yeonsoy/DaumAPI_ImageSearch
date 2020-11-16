package com.yeon.imagesearch.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yeon.imagesearch.R
import com.yeon.imagesearch.util.DaumAPI
import com.yeon.imagesearch.util.ImageModel
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val api: DaumAPI by lazy {
        DaumAPI.create()
    }
    val disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getImageList(query: String, sort: String? = "accuracy", page: Int? = 1, size: Int? = 80) {

       // disposable = api.getImageList(query, sort, page, size)
       //     .subscribeOn

        // retrofit2 만을 사용한 예외처리
        api.getImageList(query, sort, page, size).enqueue(object : Callback<ImageModel.Result> {
            override fun onResponse(call: Call<ImageModel.Result>, response: Response<ImageModel.Result>) {
                Log.d("LOG", "onResponse: 성공")
                if(response.message() != "OK")
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ImageModel.Result>, t: Throwable) {
                Log.d("LOG", "onFailure: 실패")
            }
        })
    }
}