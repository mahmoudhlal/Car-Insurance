package com.mahmoud.carsinsurance.ui.user.home.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationResponse
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationsItem
import com.mahmoud.myfirstkotlinapp.Remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationDataSource(private var token: String?) :
    PageKeyedDataSource<Int, NotificationsItem>() {

    companion object {
        private val TAG = "NotificationDataSource"
        val PAGE_SIZE = 10
        private val FIRST_PAGE = 1
    }
    private var networkState: MutableLiveData<NetworkState>? = null

    init {
        networkState = MutableLiveData()
    }


    fun getNetworkState(): MutableLiveData<NetworkState>? {
        return networkState
    }


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NotificationsItem>
    ) {
        networkState!!.postValue(NetworkState.LOADING)
        ApiUtils.getAppService().getNotifications(
            token!!,
            FIRST_PAGE
        ).enqueue(object : Callback<NotificationResponse?> {
            override fun onResponse(
                call: Call<NotificationResponse?>,
                response: Response<NotificationResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        response.body()?.data?.notifications?.let {
                            callback.onResult(
                                it,
                                null,
                                FIRST_PAGE + 1
                            )
                        }
                        networkState!!.postValue(NetworkState.LOADED)
                        Log.i(TAG,"loadInitial")
                    }
                } else {
                    networkState!!.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<NotificationResponse?>,
                t: Throwable
            ) {
                val errorMessage = t.message
                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
            }
        })
    }

    override fun loadAfter(
        params: LoadParams<Int>, callback: LoadCallback<Int, NotificationsItem>) {
        networkState!!.postValue(NetworkState.LOADING)
        ApiUtils.getAppService().getNotifications(token!!,  params.key)
            .enqueue(object : Callback<NotificationResponse?> {
                override fun onResponse(
                    call: Call<NotificationResponse?>,
                    response: Response<NotificationResponse?>
                ) {
                    Log.i(TAG,params.key.toString() + "")
                    if (response.isSuccessful) {
                        if (response.body()?.status!!) {
                            val key  =
                                response.body()!!.data?.paginate?.totalPages?.let{
                                    if (it > params.key) params.key + 1 else null
                                }
                            response.body()!!.data?.notifications?.let { callback.onResult(it, key) }
                            networkState!!.postValue(NetworkState.LOADED)
                            Log.i(TAG,"loadAfter")
                        }else{
                            Log.i(TAG,"Error : "+response.body()?.msg)
                        }
                    } else {
                        networkState!!.postValue(
                            NetworkState(NetworkState.Status.FAILED,response.message())
                        )
                    }
                }
                override fun onFailure(call: Call<NotificationResponse?>, t: Throwable) {
                    val errorMessage = t.message
                    networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                }
            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NotificationsItem>
    ) {
    }
}