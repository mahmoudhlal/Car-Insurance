package com.mahmoud.myfirstkotlinapp.Remote

import com.mahmoud.carsinsurance.models.AuthResponse.AuthResponse
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {


    @POST("login")
    fun login(
        @Query("email") userName: String,
        @Query("password") password: String
    ): Call<AuthResponse>

    @POST("register")
    fun register(
        @Query("name") name: String,
        @Query("email") userName: String,
        @Query("password") password: String,
        @Query("role") role: String,
        @Query("country") country: String,
        @Query("type") type: String
    ): Call<AuthResponse>

    @GET("companies")
    fun getCompanies(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int
    ): Call<CompaniesResponse>


    /* @GET("top-headlines")
     fun getNews(
         @Query("apiKey") apiKey: String,
         @Query("country") country: String,
         *//*@Query("sources") sources: String,*//*
        @Query("page") page: Int
    ): Call<NewsResponse>

    @GET("sources")
    fun getNewsSources(
        @Query("apiKey") apiKey: String
    ): Call<SourcesResponse>
*/

}