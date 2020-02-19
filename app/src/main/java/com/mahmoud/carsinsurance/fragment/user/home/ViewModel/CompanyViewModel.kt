package com.mahmoud.carsinsurance.fragment.user.home.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.fragment.user.home.repository.CompanyRepository
import com.mahmoud.carsinsurance.models.GeneralResponse.GeneralLiveData
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem

class CompanyViewModel(private val repository: CompanyRepository?) : ViewModel() {
    private var companiesLiveData: LiveData<PagedList<CompaniesItem>>?=null
    private var networkState: LiveData<NetworkState>? = null



    fun getCompanies(){
        companiesLiveData = repository?.getComplaints()
        networkState = repository?.getNetWorkState()
    }

    fun observeCompanies():LiveData<PagedList<CompaniesItem>>?{
        return companiesLiveData
    }
    fun observeNetwork():LiveData<NetworkState>? {
        return networkState
    }
}