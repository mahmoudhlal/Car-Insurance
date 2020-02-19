package com.mahmoud.carsinsurance.models.companiesResponse


import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("companies")
	val companies: List<CompaniesItem?>? = null,

	@field:SerializedName("paginate")
	val paginate: Paginate? = null
)