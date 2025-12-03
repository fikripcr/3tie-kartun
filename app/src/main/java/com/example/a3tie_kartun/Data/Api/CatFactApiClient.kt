package com.example.a3tie_kartun.Data.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CatFactApiClient {
    private const val BASE_URL = "https://catfact.ninja"

    val apiService: CatFactApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatFactApiService::class.java)
    }
}