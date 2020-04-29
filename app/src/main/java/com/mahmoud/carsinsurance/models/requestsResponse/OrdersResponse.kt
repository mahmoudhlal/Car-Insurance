package com.mahmoud.carsinsurance.models.requestsResponse


import com.google.gson.annotations.SerializedName


data class OrdersResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)