package com.mahmoud.carsinsurance.ui.user.company.Payment

interface PaymentOnClick {
    fun onClick(cardNum: String?,eDate: String?,name: String?,money: String?,cvv: String?)
}