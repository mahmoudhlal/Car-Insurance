package com.mahmoud.carsinsurance.fragment.user.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem

class CompanyDataSourceFactory(private var token: String?) :
    DataSource.Factory<Int, CompaniesItem>() {
    //creating mutable live data
    private val itemLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, CompaniesItem>> =
        MutableLiveData()
    private val mutableLiveData: MutableLiveData<CompanyDataSource> =
        MutableLiveData()
    private val networkStateMutableLiveData: LiveData<NetworkState>? = null

    override fun create(): DataSource<Int, CompaniesItem> { //getting source data object
        val itemDataSource = CompanyDataSource(token)
        //posting dataSource to get values
        itemLiveDataSource.postValue(itemDataSource)
        //networkStateMutableLiveData = itemDataSource.getNetworkState();
        mutableLiveData.postValue(itemDataSource)
        return itemDataSource
    }

    fun getItemLiveDataSource(): MutableLiveData<CompanyDataSource>? {
        return mutableLiveData
    }

}