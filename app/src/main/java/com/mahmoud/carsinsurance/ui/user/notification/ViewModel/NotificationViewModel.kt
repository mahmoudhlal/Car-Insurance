package com.mahmoud.carsinsurance.ui.user.home.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.notificationResponse.NotificationsItem
import com.mahmoud.carsinsurance.ui.user.home.repository.NotificationRepository

class NotificationViewModel(private val repository: NotificationRepository?) : ViewModel() {
    private var notificationLiveData: LiveData<PagedList<NotificationsItem>>? = null
    private var networkState: LiveData<NetworkState>? = null
   

    fun getNotifications() {
        notificationLiveData = repository?.getNotifications()
        networkState = repository?.getNetWorkState()
    }

    fun observeNotifications(): LiveData<PagedList<NotificationsItem>>? = notificationLiveData 

    fun observeNetwork(): LiveData<NetworkState>? = networkState

}