package com.mahmoud.carsinsurance.ui.user.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.requestsResponse.OrdersItem

class OrderDataSourceFactory(private var token: String?) :
    DataSource.Factory<Int, OrdersItem>() {
    //creating mutable live data
    private val itemLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, OrdersItem>> =
        MutableLiveData()
    private val mutableLiveData: MutableLiveData<OrderDataSource> =
        MutableLiveData()
    private val networkStateMutableLiveData: LiveData<NetworkState>? = null

    override fun create(): DataSource<Int, OrdersItem> { //getting source data object
        val itemDataSource = OrderDataSource(token)
        //posting dataSource to get values
        itemLiveDataSource.postValue(itemDataSource)
        //networkStateMutableLiveData = itemDataSource.getNetworkState();
        mutableLiveData.postValue(itemDataSource)
        return itemDataSource
    }

    fun getItemLiveDataSource(): MutableLiveData<OrderDataSource>? = mutableLiveData

}