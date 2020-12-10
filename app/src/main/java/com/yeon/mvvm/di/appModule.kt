package com.yeon.mvvm.di

import com.yeon.mvvm.util.DaumAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    single { provideOkHttpClient("KakaoAK a0f60e42766f7eb00662d86062741899") }
    single { provideRetrofit(get(), "https://dapi.kakao.com") }
    single { provideApiService(get()) }
}

private fun provideOkHttpClient(API_KEY: String) : OkHttpClient{
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    var headerInterceptor = Interceptor {
        val request = it.request()
            .newBuilder()
            .addHeader("Authorization", API_KEY)
            .build()
        return@Interceptor it.proceed((request))
    }

   return OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

private fun provideApiService(retrofit: Retrofit): DaumAPI =
    retrofit.create(DaumAPI::class.java)