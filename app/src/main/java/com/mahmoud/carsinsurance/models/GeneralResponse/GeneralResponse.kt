package com.mahmoud.carsinsurance.models.GeneralResponse

class GeneralResponse<T> {


    private var status: Status? = null
    private var data: T? = null
    private var error: Error? = null


    fun loading(): GeneralResponse<T>? {
        status = Status.Loading
        data = null
        error = null
        return this
    }

    fun success(data: T): GeneralResponse<T>? {
        status = Status.Success
        this.data = data
        error = null
        return this
    }

    fun error(error: Error?): GeneralResponse<T>? {
        status = Status.Failure
        this.error = error
        data = null
        return this
    }

    fun getStatus(): Status? {
        return status
    }

    fun setStatus(status: Status?) {
        this.status = status
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T) {
        this.data = data
    }

    fun getError(): Error? {
        return error
    }

    fun setError(error: Error?) {
        this.error = error
    }

}