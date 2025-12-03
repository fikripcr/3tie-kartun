package com.example.a3tie_kartun.Data.Api

import com.example.a3tie_kartun.Data.Model.PhotoModel
import retrofit2.http.GET

interface PhotoApiService {
    @GET("list")
    suspend fun getPhotos(): List<PhotoModel>
}