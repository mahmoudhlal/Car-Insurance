package com.mahmoud.carsinsurance.models.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var locationLiveData = MutableLiveData<Location>()
    fun getLocationLiveData(): LiveData<Location>? {return locationLiveData }

}