package com.mahmoud.carsinsurance.ui.user.home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoud.carsinsurance.ui.user.home.repository.OrderRepository

class OrderViewModelFactory
    (private val repository: OrderRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  OrderViewModel(repository) as  T
    }
}