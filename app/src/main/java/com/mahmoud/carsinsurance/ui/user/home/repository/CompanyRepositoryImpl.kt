package com.mahmoud.carsinsurance.ui.user.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import com.mahmoud.carsinsurance.ui.user.home.data.CompanyDataSource
import com.mahmoud.carsinsurance.ui.user.home.data.CompanyDataSourceFactory
import com.mahmoud.carsinsurance.ui.user.home.data.OrderDataSourceFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CompanyRepositoryImpl(private var token: String?) : CompanyRepository {

    private var networkState: LiveData<NetworkState>? = null
    private var executor: ExecutorService? = null


    override fun getComplaints(): LiveData<PagedList<CompaniesItem>> {
        executor = Executors.newFixedThreadPool(5)
        val itemDataSourceFactory =
            CompanyDataSourceFactory(token)
        //getting live data source from data source library
        //getting live data source from data source library
        networkState =
            switchMap(itemDataSourceFactory.getItemLiveDataSource()!!) { it.getNetworkState() }


        //getting PagedList config
        //getting PagedList config
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(false)
                .setPageSize(CompanyDataSource.PAGE_SIZE).build()

        //building the paged list
        //private LiveData<PageKeyedDataSource<Integer, Complaint>> liveDataSource;
        //building the paged list
        //private LiveData<PageKeyedDataSource<Integer, Complaint>> liveDataSource;
        return LivePagedListBuilder<Int, CompaniesItem>(itemDataSourceFactory, pagedListConfig)
            .setFetchExecutor(executor!!)
            .build()
    }

    override fun getNetWorkState(): LiveData<NetworkState>? {
        return networkState
    }
}