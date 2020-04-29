package com.mahmoud.carsinsurance.models.requestsResponse


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
@Parcelize
data class OrdersItem (

	@field:SerializedName("comany_id")
	val comanyId: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("car_year")
	val carYear: String? = null,

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("car_color")
	val carColor: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("car_type")
	val carType: String? = null,

	@field:SerializedName("attaches")
	val attaches: List<String?>? = null,

	@field:SerializedName("insurance_type")
	val insuranceType: String? = null,

	@field:SerializedName("car_name")
	val carName: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
): Parcelable