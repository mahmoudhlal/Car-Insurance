package com.mahmoud.carsinsurance.fragment.user.home.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesResponse
import com.mahmoud.myfirstkotlinapp.Remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyDataSource(private var token: String?) :
    PageKeyedDataSource<Int, CompaniesItem>() {

    companion object {
        private val TAG = "ComplaintsDataSource"
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
        callback: LoadInitialCallback<Int, CompaniesItem>
    ) {
        networkState!!.postValue(NetworkState.LOADING)
        ApiUtils.getAppService().getCompanies(
            token!!,
            FIRST_PAGE
        ).enqueue(object : Callback<CompaniesResponse?> {
            override fun onResponse(
                call: Call<CompaniesResponse?>,
                response: Response<CompaniesResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        response.body()?.data?.companies?.let {
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
                call: Call<CompaniesResponse?>,
                t: Throwable
            ) {
                val errorMessage = t.message
                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
            }
        })
    }

    override fun loadAfter(
        params: LoadParams<Int>, callback: LoadCallback<Int, CompaniesItem>) {
        networkState!!.postValue(NetworkState.LOADING)
        ApiUtils.getAppService().getCompanies(token!!,  params.key)
            .enqueue(object : Callback<CompaniesResponse?> {
                override fun onResponse(
                    call: Call<CompaniesResponse?>,
                    response: Response<CompaniesResponse?>
                ) {
                    Log.i(TAG,params.key.toString() + "")
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val key =
                                if (response.body()!!.data?.paginate?.totalPages!! > params.key)
                                    params.key + 1 else null
                            response.body()!!.data?.companies?.let { callback.onResult(it, key) }
                            networkState!!.postValue(NetworkState.LOADED)
                            Log.i(TAG,"loadAfter")
                        }
                    } else {
                        networkState!!.postValue(
                            NetworkState(NetworkState.Status.FAILED,response.message())
                        )
                    }
                }
                override fun onFailure(call: Call<CompaniesResponse?>, t: Throwable) {
                    val errorMessage = t.message
                    networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                }
            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CompaniesItem>
    ) {
    }
}