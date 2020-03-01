package com.mahmoud.carsinsurance.ui.user.home.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.GeneralResponse.GeneralLiveData
import com.mahmoud.carsinsurance.models.addOrderResponse.AddOrderResponse
import com.mahmoud.carsinsurance.models.requestsResponse.OrdersItem
import com.mahmoud.carsinsurance.ui.user.home.repository.OrderRepository
import com.mahmoud.myfirstkotlinapp.Remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel(private val repository: OrderRepository?) : ViewModel() {
    private var orderLiveData: LiveData<PagedList<OrdersItem>>? = null
    private var networkState: LiveData<NetworkState>? = null
    private var handleOrder : GeneralLiveData<AddOrderResponse> = GeneralLiveData() 
   
    fun getOrders() {
        orderLiveData = repository?.getOrders()
        networkState = repository?.getNetWorkState()
    }

    fun observeOrders(): LiveData<PagedList<OrdersItem>>? = orderLiveData
    

    fun observeNetwork(): LiveData<NetworkState>? = networkState

    fun observeHandleOrder(): GeneralLiveData<AddOrderResponse>? = handleOrder

    fun handleOrder(
        Authorization: String?,
        id: Int?,
        status: Int?,
        message: String?
    ) {
        handleOrder.postLoading()
        ApiUtils.getAppService().handleOrder(Authorization,id, 
            status, message
        )?.enqueue(object : Callback<AddOrderResponse?> {
            override fun onResponse(
                call: Call<AddOrderResponse?>,
                response: Response<AddOrderResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        handleOrder.postSuccess(response.body()!!)
                    }else{
                        handleOrder.postError(
                            com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                                response.body()?.data,
                                null,
                                response.code(),
                                true
                            )
                        )
                    }
                } else {
                    handleOrder.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                        response.message(),
                        null,
                        response.code(),
                        true
                    ))
                }
            }

            override fun onFailure(
                call: Call<AddOrderResponse?>,
                t: Throwable
            ) {
                val errorMessage = t.message
                handleOrder.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                    errorMessage,
                    t,
                    0,
                    true
                ))
            }
        })
    }





}