package com.mahmoud.carsinsurance.models.GeneralResponse

import androidx.lifecycle.MutableLiveData

class GeneralLiveData<T> : MutableLiveData<GeneralResponse<T>>() {
    fun postLoading() {
        postValue(GeneralResponse<T>().loading())
    }

    fun postError(error: Error?) {
        postValue(GeneralResponse<T>().error(error))
    }

    fun postSuccess(data: T) {
        postValue(GeneralResponse<T>().success(data))
    }


}