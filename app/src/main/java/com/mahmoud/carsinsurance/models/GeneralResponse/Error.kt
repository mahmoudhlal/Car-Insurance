package com.mahmoud.carsinsurance.models.GeneralResponse

class Error(var msg: String?, var throwable: Throwable?, var responseCode: Int?,
            var isToast: Boolean?
)