package com.mahmoud.carsinsurance.models.AuthResponse


import com.google.gson.annotations.SerializedName
import com.mahmoud.carsinsurance.models.AuthResponse.Data


data class AuthResponse(

    @field:SerializedName("data")
	val data: Data? = null,

    @field:SerializedName("status")
	val status: Boolean? = null,

    @field:SerializedName("msg")
    val msg: String? = null

)