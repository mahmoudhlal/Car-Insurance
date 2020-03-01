package com.mahmoud.myfirstkotlinapp.Remote

import com.mahmoud.carsinsurance.models.AuthResponse.AuthResponse
import com.mahmoud.carsinsurance.models.addOrderResponse.AddOrderResponse
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesResponse
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationResponse
import com.mahmoud.carsinsurance.models.requestsResponse.OrdersResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

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
        @Query("type") type: String,
        @Query("details") details: String
    ): Call<AuthResponse>

    @GET("companies")
    fun getCompanies(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int
    ): Call<CompaniesResponse>

    @GET("notifications")
    fun getNotifications(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int
    ): Call<NotificationResponse>

    @GET("orders")
    fun getOrders(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int
    ): Call<OrdersResponse>

    @Multipart
    @POST("add-order/{id}")
    fun addOrder(
        @Header("Authorization") Authorization: String?,
        @Path("id") id: Int?,
        @Query("type") type: String?,
        @Query("insurance_type") insurance_type: String?,
        @Query("car_name") car_name: String?,
        @Query("car_year") car_year: String?,
        @Query("car_color") car_color: String?,
        @Query("car_type") car_type: String?,
        @Query("card_no") card_no: String?,
        @Query("expire_date") expire_date: String?,
        @Query("name") name: String?,
        @Query("amount") amount: String?,
        @Query("cvv") cvv: String?,
        @Part attach: Array<MultipartBody.Part?>?
    ): Call<AddOrderResponse?>?

    @Multipart
    @POST("add-order/{id}")
    fun addOrder(
        @Header("Authorization") Authorization: String?,
        @Path("id") id: Int?,
        @Query("type") type: String?,
        @Query("address") address: String?,
        @Query("lat") lat: String?,
        @Query("lng") lng: String?,
        @Query("card_no") card_no: String?,
        @Query("expire_date") expire_date: String?,
        @Query("name") name: String?,
        @Query("amount") amount: String?,
        @Query("cvv") cvv: String?,
        @Part attach: Array<MultipartBody.Part?>?
    ): Call<AddOrderResponse?>?

    @POST("handle-order/{id}")
    fun handleOrder(
        @Header("Authorization") Authorization: String?,
        @Path("id") id: Int?,
        @Query("status") status: Int?,
        @Query("message") message: String?
    ): Call<AddOrderResponse?>?

  @POST("approve/{id}")
    fun approve(
        @Header("Authorization") Authorization: String?,
        @Path("id") id: Int?,
        @Query("status") status: Int?
    ): Call<AddOrderResponse?>?


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