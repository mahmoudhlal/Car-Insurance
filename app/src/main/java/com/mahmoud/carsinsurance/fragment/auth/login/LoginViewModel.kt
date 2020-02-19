package com.mahmoud.carsinsurance.fragment.auth.login

import androidx.lifecycle.ViewModel
import com.mahmoud.carsinsurance.models.AuthResponse.AuthResponse
import com.mahmoud.carsinsurance.models.AuthResponse.Data
import com.mahmoud.carsinsurance.models.GeneralResponse.Error
import com.mahmoud.carsinsurance.models.GeneralResponse.GeneralLiveData
import com.mahmoud.myfirstkotlinapp.Remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val source: GeneralLiveData<Data?> = GeneralLiveData()

    fun login(email: String, pass: String) {
        source.postLoading()
        ApiUtils.getAppService().login(email, pass)
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful()) {
                        if (response.body()?.status!!) {
                            source.postSuccess(response.body()?.data)
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
                        source.postError(Error(response.message(),
                            null, null, true))
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