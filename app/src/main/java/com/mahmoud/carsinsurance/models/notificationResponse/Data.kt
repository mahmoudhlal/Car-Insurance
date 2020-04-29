package com.mahmoud.carsinsurance.models.notificationResponse

data class Data(
	val paginate: Paginate? = null,
	val notifications: List<NotificationsItem?>? = null,
	val title: String? = null,
	val message: String? = null
)
