package com.mahmoud.carsinsurance.models.requestsResponse


import com.google.gson.annotations.SerializedName


data class Data(

	@field:SerializedName("paginate")
	val paginate: Paginate? = null,

	@field:SerializedName("orders")
	val orders: List<OrdersItem?>? = null
)