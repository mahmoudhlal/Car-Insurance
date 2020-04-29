package com.mahmoud.carsinsurance.ui.user.home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoud.carsinsurance.ui.user.home.repository.NotificationRepository

class NotificationViewModelFactory(private val repository: NotificationRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  NotificationViewModel(repository) as  T

    }
}