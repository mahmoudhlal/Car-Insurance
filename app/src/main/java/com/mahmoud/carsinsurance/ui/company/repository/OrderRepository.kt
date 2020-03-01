package com.mahmoud.carsinsurance.ui.user.home.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import com.mahmoud.carsinsurance.models.requestsResponse.OrdersItem

interface OrderRepository {
    fun getOrders(): LiveData<PagedList<OrdersItem>>
    fun getNetWorkState(): LiveData<NetworkState>?
}