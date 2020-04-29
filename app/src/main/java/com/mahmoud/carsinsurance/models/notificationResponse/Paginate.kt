package com.mahmoud.carsinsurance.models.notificationResponse

data class Paginate(
	val perPage: Int? = null,
	val total: Int? = null,
	val count: Int? = null,
	val nextPageUrl: Any? = null,
	val totalPages: Int? = null,
	val prevPageUrl: Any? = null,
	val currentPage: Int? = null
)
