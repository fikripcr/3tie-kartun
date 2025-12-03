package com.example.a3tie_kartun.Data.Api

import com.example.a3tie_kartun.Data.Model.CatFactModel
import retrofit2.http.GET

interface CatFactApiService {
    @GET("fact")
    suspend fun getCatFact(): CatFactModel
}