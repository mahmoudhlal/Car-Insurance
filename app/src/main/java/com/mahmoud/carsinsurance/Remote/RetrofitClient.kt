package com.mahmoud.myfirstkotlinapp.Remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    var retrofit: Retrofit? = null
    internal fun getClient(): Retrofit? {

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("http://moolnow.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(setTime())
                .build()

        }
        return retrofit
    }

    /*fun setRetrofit(retrofit: Retrofit) {
        this.retrofit = retrofit
    }
*/
    private fun setTime(): OkHttpClient {

        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}