package com.mahmoud.carsinsurance.ui.user.home.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mahmoud.carsinsurance.Utils.NetworkState
import com.mahmoud.carsinsurance.models.GeneralResponse.GeneralLiveData
import com.mahmoud.carsinsurance.models.addOrderResponse.AddOrderResponse
import com.mahmoud.carsinsurance.models.companiesResponse.CompaniesItem
import com.mahmoud.carsinsurance.models.notificationCount.NotificationCount
import com.mahmoud.carsinsurance.ui.user.home.repository.CompanyRepository
import com.mahmoud.carsinsurance.ui.user.home.repository.OrderRepository
import com.mahmoud.myfirstkotlinapp.Remote.ApiUtils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class CompanyViewModel(private val repository: CompanyRepository?) : ViewModel() {
    private var companiesLiveData: LiveData<PagedList<CompaniesItem>>? = null
    private var networkState: LiveData<NetworkState>? = null
    private var addOrder: GeneralLiveData<AddOrderResponse> = GeneralLiveData()
    private var addRenewOrder: GeneralLiveData<AddOrderResponse> = GeneralLiveData()
    private var approveCompanyRequest: GeneralLiveData<AddOrderResponse> = GeneralLiveData()
    var noCount: GeneralLiveData<NotificationCount> = GeneralLiveData()


    fun getCompanies() {
        companiesLiveData = repository?.getComplaints()
        networkState = repository?.getNetWorkState()
    }

    fun observeCompanies(): LiveData<PagedList<CompaniesItem>>? {
        return companiesLiveData
    }

    fun observeNetwork(): LiveData<NetworkState>? {
        return networkState
    }

    fun observeAddOrder(): GeneralLiveData<AddOrderResponse>? = addOrder

    fun observeRenewOrder(): GeneralLiveData<AddOrderResponse>? = addRenewOrder

    fun addOrder(
        Authorization: String?,
        id: Int?,
        type: String?,
        insurance_type: String?,
        car_name: String?,
        car_year: String?,
        car_color: String?,
        car_type: String?,
        card_no: String?,
        expire_date: String?,
        name: String?,
        amount: String?,
        cvv: String?,
        attach: Array<MultipartBody.Part?>?
    ) {
        addOrder.postLoading()
        ApiUtils.getAppService().addOrder(Authorization,id, type,
            insurance_type, car_name, car_year, car_color,
            car_type, card_no ,expire_date, name, amount, cvv ,attach
        )?.enqueue(object : Callback<AddOrderResponse?> {
            override fun onResponse(
                call: Call<AddOrderResponse?>,
                response: Response<AddOrderResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.status!!) {
                        addOrder.postSuccess(response.body()!!)
                    }else{
                        addOrder.postError(
                            com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                                response.body()?.msg,
                                null,
                                response.code(),
                                true
                            )
                        )
                    }
                } else {
                    addOrder.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
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
                addOrder.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                    errorMessage,
                    t,
                    0,
                    true
                ))
            }
        })
    }


    fun addOrder(
        Authorization: String?,
        id: Int?,
        type: String?,
        address: String?,
        lat: String?,
        lng: String?,
        card_no: String?,
        expire_date: String?,
        name: String?,
        amount: String?,
        cvv: String?,
        attach: Array<MultipartBody.Part?>?
    ) {
        addRenewOrder.postLoading()
        ApiUtils.getAppService().addOrder(Authorization,id, type,address, lat, lng,
            card_no,expire_date, name, amount, cvv ,attach)
            ?.enqueue(object : Callback<AddOrderResponse?> {
            override fun onResponse(
                call: Call<AddOrderResponse?>,
                response: Response<AddOrderResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.status!!) {
                        addRenewOrder.postSuccess(response.body()!!)
                    }else{
                        addRenewOrder.postError(
                            com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                                response.body()?.msg,
                                null,
                                response.code(),
                                true
                            )
                        )
                    }
                } else {
                    addRenewOrder.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
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
                addRenewOrder.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                    errorMessage,
                    t,
                    0,
                    true
                ))
            }
        })
    }

    fun observeApproval():GeneralLiveData<AddOrderResponse> = approveCompanyRequest

    fun approveToCompanyRequest(
        Authorization: String?,
        id: Int?,
        status: Int?
    ) {
        approveCompanyRequest.postLoading()
        ApiUtils.getAppService().approve(Authorization,id,status)
            ?.enqueue(object : Callback<AddOrderResponse?> {
            override fun onResponse(
                call: Call<AddOrderResponse?>,
                response: Response<AddOrderResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.status!!) {
                        approveCompanyRequest.postSuccess(response.body()!!)
                    }else{
                        approveCompanyRequest.postError(
                            com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                                response.body()?.msg,
                                null,
                                response.code(),
                                true
                            )
                        )
                    }
                } else {
                    approveCompanyRequest.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
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
                approveCompanyRequest.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                    errorMessage,
                    t,
                    0,
                    true
                ))
            }
        })
    }

  fun getNotificationCount(Authorization: String?) {
      noCount.postLoading()
      ApiUtils.getAppService().getNotificationsCount(Authorization)
          .enqueue(object : Callback<NotificationCount?> {
              override fun onResponse(
                  call: Call<NotificationCount?>,
                  response: Response<NotificationCount?>
              ) {
                  if (response.isSuccessful) {
                      if (response.body()?.status!!) {
                          noCount.postSuccess(response.body()!!)
                      }else{
                          noCount.postError(
                              com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                                  response.body()?.msg,
                                  null,
                                  response.code(),
                                  true
                              )
                          )
                      }
                  } else {
                      noCount.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                          response.message(),
                          null,
                          response.code(),
                          true
                      ))
                  }
              }

              override fun onFailure(
                  call: Call<NotificationCount?>,
                  t: Throwable
              ) {
                  val errorMessage = t.message
                  noCount.postError( com.mahmoud.carsinsurance.models.GeneralResponse.Error(
                      errorMessage,
                      t,
                      0,
                      true
                  ))
              }
          })
    }

}