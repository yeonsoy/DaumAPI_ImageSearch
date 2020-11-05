package com.yeon.imagesearch.api

import android.content.Context
import com.yeon.imagesearch.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit API with RxJava
// https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2
class RetrofitManager {
    companion object  {
        fun getKakaoAPI(): RestAPI {
            val logging = HttpLoggingInterceptor()
            val httpClient = OkHttpClient.Builder()

            val API_KEY = "KakaoAK a0f60e42766f7eb00662d86062741899"
            val BASE_URL = "https://dapi.kakao.com/"

            // gson : json을 java객체로 만들어주는 라이브러리
            val gson = GsonBuilder()
                .setLenient()
                .create()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder  = original.newBuilder().header("Authorization", API_KEY)
                val request = requestBuilder.build()
                chain.proceed(request)
            }

            if(BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logging)
            }
            else {
                logging.level = HttpLoggingInterceptor.Level.HEADERS
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            return retrofit.create(RestAPI::class.java)
        }

        fun getWidth(context: Context): Int {
            val dm = context.applicationContext.resources.displayMetrics
            return dm.widthPixels
        }

        fun getHeight(context: Context): Int {
            val dm = context.applicationContext.resources.displayMetrics
            return dm.heightPixels
        }

        fun  getRatioHeight(context: Context, originHeight : Int, originWidth : Int) : Int{
            return (originHeight.toFloat() * getWidth(context) / originWidth.toFloat()).toInt()
        }
    }

}