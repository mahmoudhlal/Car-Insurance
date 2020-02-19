package com.mahmoud.carsinsurance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mahmoud.carsinsurance.Utils.Constants
import com.mahmoud.carsinsurance.Utils.SharedPrefManager
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPrefManager.getInstance(applicationContext)?.getUserData()
            .let { Constants.CURRENT_ROLE = it?.role }
    }
}
