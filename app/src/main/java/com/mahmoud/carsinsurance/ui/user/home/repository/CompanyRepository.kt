package com.mahmoud.carsinsurance.ui.user.home.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem

interface CompanyRepository {
    fun getComplaints(): LiveData<PagedList<CompaniesItem>>
    fun getNetWorkState(): LiveData<NetworkState>?
}