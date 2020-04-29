package com.mahmoud.carsinsurance.ui.user.home.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationsItem

interface NotificationRepository {
    fun getNotifications(): LiveData<PagedList<NotificationsItem>>
    fun getNetWorkState(): LiveData<NetworkState>?
}