package com.mahmoud.myfirstkotlinapp.Remote

class ApiUtils {

    companion object {

        fun getAppService(): AppService {
            val retrofitClient = RetrofitClient()
            return retrofitClient.getClient()!!.create(AppService::class.java)
        }

    }
}