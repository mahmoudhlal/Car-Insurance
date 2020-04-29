package com.mahmoud.carsinsurance.ui.user.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationsItem

class NotificationDataSourceFactory(private var token: String?) :
    DataSource.Factory<Int, NotificationsItem>() {
    //creating mutable live data
    private val itemLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, NotificationsItem>> =
        MutableLiveData()
    private val mutableLiveData: MutableLiveData<NotificationDataSource> =
        MutableLiveData()
    private val networkStateMutableLiveData: LiveData<NetworkState>? = null

    override fun create(): DataSource<Int, NotificationsItem> { //getting source data object
        val itemDataSource = NotificationDataSource(token)
        //posting dataSource to get values
        itemLiveDataSource.postValue(itemDataSource)
        //networkStateMutableLiveData = itemDataSource.getNetworkState();
        mutableLiveData.postValue(itemDataSource)
        return itemDataSource
    }

    fun getItemLiveDataSource(): MutableLiveData<NotificationDataSource>? {
        return mutableLiveData
    }

}