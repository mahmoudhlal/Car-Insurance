package com.mahmoud.carsinsurance.models.notificationCount

import com.google.gson.annotations.SerializedName

data class NotificationCount(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)