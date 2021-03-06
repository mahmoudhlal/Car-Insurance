package com.mahmoud.carsinsurance.ui.user.home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoud.carsinsurance.ui.user.home.repository.CompanyRepository

class CompanyViewModelFactory
    (private val repository: CompanyRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  CompanyViewModel(repository) as  T
    }
}