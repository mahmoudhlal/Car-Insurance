package com.mahmoud.carsinsurance.ui.user.home.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.ui.user.home.data.NotificationDataSourceFactory
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationsItem
import com.mahmoud.carsinsurance.ui.user.home.data.NotificationDataSource
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.lifecycle.Transformations.switchMap as switchMap

class NotificationRepositoryImpl(private var token: String?) : NotificationRepository {

    private var networkState: LiveData<NetworkState>? = null
    private var executor: ExecutorService? = null


    override fun getNotifications(): LiveData<PagedList<NotificationsItem>> {
        executor = Executors.newFixedThreadPool(5)
        val itemDataSourceFactory =
            NotificationDataSourceFactory(token)
        //getting live data source from data source library
        //getting live data source from data source library
        networkState = switchMap(itemDataSourceFactory.getItemLiveDataSource()!!){it.getNetworkState() }


        //getting PagedList config
        //getting PagedList config
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(false)
                .setPageSize(NotificationDataSource.PAGE_SIZE).build()

        //building the paged list
        //private LiveData<PageKeyedDataSource<Integer, Complaint>> liveDataSource;
        //building the paged list
        //private LiveData<PageKeyedDataSource<Integer, Complaint>> liveDataSource;
        return LivePagedListBuilder<Int, NotificationsItem>(itemDataSourceFactory, pagedListConfig)
            .setFetchExecutor(executor!!)
            .build()
    }

    override fun getNetWorkState(): LiveData<NetworkState>? {
        return networkState
    }
}