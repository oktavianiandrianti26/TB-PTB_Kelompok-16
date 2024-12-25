package com.example.presensiapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
//    private const val BASE_URL = "http://192.168.56.1:3000/api/"
//    private const val BASE_URL = "http://172.20.10.3:3000/api/"
    val BASE_URL = "http://172.20.10.4:3000/api/"
    val BASE_URL_WITHOUT_API = "http://172.20.10.4.86:3000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}