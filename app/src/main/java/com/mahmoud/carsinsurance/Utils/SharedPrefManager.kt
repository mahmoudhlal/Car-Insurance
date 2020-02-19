package com.mahmoud.carsinsurance.Utils

import android.content.Context
import com.google.gson.Gson
import com.mahmoud.carsinsurance.models.AuthResponse.Data

class SharedPrefManager {
    val SHARED_PREF_NAME = "insurance"
    val LOGIN_STATUS = "login_status"
    val IS_USER = "is_user"
    val FIRST_TIME = "shared_first_time"
    val TOKEN = "token"


    companion object {
        private var mContext: Context? = null
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(context: Context?): SharedPrefManager? {
            mContext = context
            if (mInstance == null) {
                mInstance = SharedPrefManager()
            }
            return mInstance
        }
    }


    fun getLoginStatus(): Boolean {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        return sharedPreferences.getBoolean(LOGIN_STATUS, false)
    }

    fun setLoginStatus(status: Boolean?) {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME,
            0
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOGIN_STATUS, status!!)
        editor.apply()
    }

    fun setIsUser(isUser: Boolean?) {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME,
            0
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_USER, isUser!!)
        editor.apply()
    }

    fun getIsUser(): Boolean? {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        return sharedPreferences.getBoolean(IS_USER, false)
    }

    fun isFirstTime(): Boolean? {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        return sharedPreferences.getBoolean(FIRST_TIME, true)
    }

    fun setFirstTime(status: Boolean?) {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME,
            0
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(FIRST_TIME, status!!)
        editor.apply()
    }


    /**
     * this method is responsible for user logout and clearing cache
     */
    fun Logout() {
        val sharedPreferences =
            mContext!!.getSharedPreferences(SHARED_PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        setFirstTime(false)
        setLoginStatus(false)
    }


    fun getUserData(): Data? {
        val prefs =
            mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("data", "")
        return gson.fromJson<Data>(json, Data::class.java)
    }

    fun setUserData(data: Data) {
        data.token?.let { setToken(it) }
        val editor =
            mContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit()
        val gson = Gson()
        val json = gson.toJson(data)
        editor.putString("data", json)
        editor.apply()
    }

    private fun setToken(token: String) {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME,
            0
        )
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN, "Bearer $token")
        editor.apply()
    }

    fun getToken(): String? {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_PREF_NAME, 0
        )
        return sharedPreferences.getString(TOKEN, "")
    }


}