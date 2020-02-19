package com.mahmoud.carsinsurance.models.companiesResponse


import com.google.gson.annotations.SerializedName


data class CompaniesResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)