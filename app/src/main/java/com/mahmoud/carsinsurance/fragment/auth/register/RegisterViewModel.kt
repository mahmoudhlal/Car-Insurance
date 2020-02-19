package com.mahmoud.carsinsurance.fragment.auth.register

import androidx.lifecycle.ViewModel
import com.mahmoud.carsinsurance.models.AuthResponse.AuthResponse
import com.mahmoud.carsinsurance.models.AuthResponse.Data
import com.mahmoud.carsinsurance.models.GeneralResponse.Error
import com.mahmoud.carsinsurance.models.GeneralResponse.GeneralLiveData
import com.mahmoud.myfirstkotlinapp.Remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val source: GeneralLiveData<Data?> = GeneralLiveData()

    fun register(
        name: String,
        email: String,
        country: String,
        pass: String,
        role: String,
        type: String
    ) {
        source.postLoading()
        ApiUtils.getAppService().register(name , email, pass , role , country , type)
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful()) {
                        if (response.body()?.status!!) {
                            source.postSuccess(response.body()!!.data)
                        } else {
                            source.postError(
                                Error(
                                    response.body()!!.msg,
                                    null,
                                    null,
                                    true
                                )
                            )
                        }
                    } else {
                        source.postError(Error(response.message(), null, null, true))
                    }
                }

                override fun onFailure(
                    call: Call<AuthResponse>,
                    t: Throwable
                ) {
                    source.postError(Error(null, t, null, true))
                }
            })
    }

    fun getSource(): GeneralLiveData<Data?>? {
        return source
    }
}