package com.mahmoud.carsinsurance.fragment.user.home.repository

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.fragment.user.home.data.CompanyDataSource
import com.mahmoud.carsinsurance.fragment.user.home.data.CompanyDataSourceFactory
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.lifecycle.Transformations.switchMap as switchMap

class CompanyRepositoryImpl(private var token: String?) : CompanyRepository {

    private var networkState: LiveData<NetworkState>? = null
    private var executor: ExecutorService? = null


    override fun getComplaints(): LiveData<PagedList<CompaniesItem>> {
        executor = Executors.newFixedThreadPool(5)
        val itemDataSourceFactory =
            CompanyDataSourceFactory(token)
        //getting live data source from data source library
        //getting live data source from data source library
        networkState = switchMap(itemDataSourceFactory.getItemLiveDataSource()!!){it.getNetworkState() }


        //getting PagedList config
        //getting PagedList config
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(true)
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