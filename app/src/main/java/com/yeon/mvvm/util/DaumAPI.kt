package com.yeon.mvvm.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.yeon.mvvm.model.Documents
import com.yeon.mvvm.model.Result
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface DaumAPI {
  @GET("v2/search/image")
  fun getImageList (
    @Query("query") query: String, // 검색을 원하는 질의어
    @Query("query") sort: String? = "accuracy", // 결과 문서 정렬 방식 (accuracy : 정확도순, recency : 최신순)
    @Query("query") page: Int? = 1, // 결과 페이지 번호 (1 ~ 50)
    @Query("query") size: Int? = 80
  ): Call<Result>
  // Observable<ImageModel.Result>

  companion object {
    private const val BASE_URL = "https://dapi.kakao.com"
    private const val API_KEY = "KakaoAK a0f60e42766f7eb00662d86062741899"

    fun create(): DaumAPI {
      val httpLoggingInterceptor = HttpLoggingInterceptor()
      httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

      var headerInterceptor = Interceptor {
        val request = it.request()
          .newBuilder()
          .addHeader("Authorization", API_KEY)
          .build()
        return@Interceptor it.proceed((request))
      }

      val client = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

      return Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Observable<T> 객체나 Single<T> 객체를 리턴할 수 있게 된다.
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DaumAPI::class.java)
    }
  }
}